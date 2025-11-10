package br.com.apioficina.service; // Seu pacote

import br.com.apioficina.dto.OrdemDeServicoDTO;
import br.com.apioficina.dto.PecaDTO;
import br.com.apioficina.dto.ServicoDTO;
import br.com.apioficina.dto.StatusUpdateDTO;
import br.com.apioficina.model.*;
import br.com.apioficina.model.enums.StatusOS;
import br.com.apioficina.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// --- Imports do KAFKA ---
import br.com.apioficina.config.KafkaTopicConfig; // (O nome do tópico)
import br.com.apioficina.dto.OrcamentoEmailDTO; // (O DTO do e-mail)

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdemDeServicoService {

    @Autowired
    private OrdemDeServicoRepository osRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private PecaRepository pecaRepository;

    // --- 1. ALTERAÇÃO KAFKA ---
    // Corrigido para <String, Object> para enviar nosso DTO como JSON
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public OrdemDeServicoDTO criarOrdemDeServico(OrdemDeServicoDTO ordemDeServicoDTO) {

        // --- 1. Buscar as entidades pelos IDs CORRETOS ---
        ClienteEntity cliente = clienteRepository.findById(ordemDeServicoDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + ordemDeServicoDTO.getClienteId()));

        VeiculoEntity veiculo = veiculoRepository.findById(ordemDeServicoDTO.getVeiculoId())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado com ID: " + ordemDeServicoDTO.getVeiculoId()));

        List<ServicoEntity> servicos = servicoRepository.findAllById(ordemDeServicoDTO.getServicosIds());
        List<PecaEntity> pecas = pecaRepository.findAllById(ordemDeServicoDTO.getPecasIds());

        // --- 2. Calcular o Orçamento (Valor Total)  ---
        BigDecimal valorServicos = servicos.stream()
                .map(ServicoEntity::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorPecas = pecas.stream()
                .map(PecaEntity::getPreco)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorTotal = valorServicos.add(valorPecas);

        // --- 3. Converter DTO para Entidade OS ---
        OrdemDeServicoEntity osEntity = new OrdemDeServicoEntity();
        osEntity.setCliente(cliente);
        osEntity.setVeiculo(veiculo);
        osEntity.setServicos(servicos);
        osEntity.setPecas(pecas);
        osEntity.setValorTotal(valorTotal);
        osEntity.setStatus(StatusOS.RECEBIDA); // Status inicial
        osEntity.setDataEntrada(LocalDateTime.now());

        // --- 4. Salvar no banco ---
        OrdemDeServicoEntity osSalva = osRepository.save(osEntity);

        // --- 2. ALTERAÇÃO KAFKA ---
        // (Lógica de envio do e-mail)
        try {
            // 1. Preparar a mensagem
            OrcamentoEmailDTO mensagemEmail = new OrcamentoEmailDTO(
                    osSalva.getId(),
                    osSalva.getCliente().getNome(),
                    osSalva.getCliente().getEmail(), // Pega o e-mail do cliente
                    osSalva.getVeiculo().getPlaca(),
                    osSalva.getValorTotal()
            );

            // 2. Enviar a mensagem para o tópico
            kafkaTemplate.send(KafkaTopicConfig.TOPIC_NAME, mensagemEmail);

        } catch (Exception e) {
            // Se o Kafka estiver offline, apenas logamos o erro
            System.err.println("ERRO AO ENVIAR MENSAGEM KAFKA: " + e.getMessage());
        }
        // --- FIM DA LÓGICA DO KAFKA ---

        // --- 5. Converter Entidade Salva para DTO de Resposta ---
        return converterEntidadeParaDTO(osSalva);
    }

    // --- Listar Todas as OS ---
    public List<OrdemDeServicoDTO> listarTodasOS() {
        List<OrdemDeServicoEntity> listaDeEntidades = osRepository.findAll();
        return listaDeEntidades.stream()
                .map(this::converterEntidadeParaDTO)
                .collect(Collectors.toList());
    }

    // --- Buscar OS por ID ---
    public OrdemDeServicoDTO buscarOSPorId(Long id) {
        OrdemDeServicoEntity osEntidade = osRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada com ID: " + id));
        return converterEntidadeParaDTO(osEntidade);
    }


    // --- MÉTODO ATUALIZAR STATUS (COM CONTROLE DE ESTOQUE) ---
    @Transactional
    public OrdemDeServicoDTO atualizarStatus(Long id, StatusUpdateDTO statusUpdateDTO) {

        OrdemDeServicoEntity osEntidade = osRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de Serviço não encontrada com ID: " + id));

        StatusOS novoStatus = statusUpdateDTO.getNovoStatus();
        StatusOS statusAntigo = osEntidade.getStatus(); // Pega o status ANTES de mudar

        // --- INÍCIO DA LÓGICA DE CONTROLE DE ESTOQUE ---
        if (novoStatus == StatusOS.EM_EXECUCAO && statusAntigo != StatusOS.EM_EXECUCAO) {

            List<PecaEntity> pecasParaDarBaixa = osEntidade.getPecas();

            for (PecaEntity peca : pecasParaDarBaixa) {
                Integer estoqueAtual = peca.getQuantidadeEstoque();

                if (estoqueAtual == null || estoqueAtual <= 0) {
                    throw new RuntimeException("Estoque da peça '" + peca.getDescricao() + "' está indisponível!");
                }

                peca.setQuantidadeEstoque(estoqueAtual - 1);
                pecaRepository.save(peca); // Salva a peça com estoque atualizado
            }
        }
        // --- FIM DA LÓGICA DE CONTROLE DE ESTOQUE ---


        // --- INÍCIO DA LÓGICA DO TEMPO DE EXECUÇÃO ---
        osEntidade.setStatus(novoStatus); // Atualiza o status da OS
        if ((novoStatus == StatusOS.FINALIZADA || novoStatus == StatusOS.ENTREGUE)
                && osEntidade.getDataSaida() == null) {

            osEntidade.setDataSaida(LocalDateTime.now());
        }
        // --- FIM DA LÓGICA DE TEMPO ---

        // Salva a OS (com o novo status e/ou dataSaida)
        OrdemDeServicoEntity osSalva = osRepository.save(osEntidade);

        return converterEntidadeParaDTO(osSalva);
    }
    // --- FIM DO MÉTODO ATUALIZAR STATUS ---


    // --- Calcular Tempo Médio ---
    public String getTempoMedioExecucao() {
        List<OrdemDeServicoEntity> osFinalizadas = osRepository.findAll().stream()
                .filter(os -> (os.getStatus() == StatusOS.FINALIZADA || os.getStatus() == StatusOS.ENTREGUE)
                        && os.getDataEntrada() != null && os.getDataSaida() != null)
                .collect(Collectors.toList());

        if (osFinalizadas.isEmpty()) {
            return "Nenhuma Ordem de Serviço finalizada ainda.";
        }

        long totalMinutos = 0;
        for (OrdemDeServicoEntity os : osFinalizadas) {
            Duration duracao = Duration.between(os.getDataEntrada(), os.getDataSaida());
            totalMinutos += duracao.toMinutes();
        }

        long mediaEmMinutos = totalMinutos / osFinalizadas.size();
        long horas = mediaEmMinutos / 60;
        long minutos = mediaEmMinutos % 60;

        return String.format("Tempo médio de execução: %d horas e %d minutos (baseado em %d OS).",
                horas, minutos, osFinalizadas.size());
    }


    // --- MÉTODO (AUXILIAR): Conversor ---
    private OrdemDeServicoDTO converterEntidadeParaDTO(OrdemDeServicoEntity osSalva) {
        OrdemDeServicoDTO respostaDTO = new OrdemDeServicoDTO();
        respostaDTO.setId(osSalva.getId());
        respostaDTO.setStatus(osSalva.getStatus());
        respostaDTO.setDataEntrada(osSalva.getDataEntrada());
        respostaDTO.setDataSaida(osSalva.getDataSaida());
        respostaDTO.setValorTotal(osSalva.getValorTotal());
        respostaDTO.setClienteNome(osSalva.getCliente().getNome());
        respostaDTO.setVeiculoPlaca(osSalva.getVeiculo().getPlaca());

        List<ServicoDTO> servicosDTOList = osSalva.getServicos().stream()
                .map(servico -> {
                    ServicoDTO dto = new ServicoDTO();
                    dto.setDescricao(servico.getDescricao());
                    dto.setPreco(servico.getPreco());
                    return dto;
                }).collect(Collectors.toList());
        respostaDTO.setServicos(servicosDTOList);

        List<PecaDTO> pecasDTOList = osSalva.getPecas().stream()
                .map(peca -> {
                    PecaDTO dto = new PecaDTO();
                    dto.setDescricao(peca.getDescricao());
                    dto.setPreco(peca.getPreco());
                    dto.setQuantidadeEstoque(peca.getQuantidadeEstoque());
                    return dto;
                }).collect(Collectors.toList());
        respostaDTO.setPecas(pecasDTOList);

        return respostaDTO;
    }

} // Fim da classe