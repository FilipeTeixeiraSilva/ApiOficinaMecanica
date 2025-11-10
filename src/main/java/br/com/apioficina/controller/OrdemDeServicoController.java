package br.com.apioficina.controller; // Verifique o seu pacote

import br.com.apioficina.dto.OrdemDeServicoDTO;
import br.com.apioficina.dto.StatusUpdateDTO;
import br.com.apioficina.service.OrdemDeServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importe este

import java.util.List;

@RestController
@RequestMapping("/ordem-de-servico") // Seu path está correto
public class OrdemDeServicoController {

    @Autowired
    private OrdemDeServicoService ordemDeServicoService;

    @PostMapping
    public ResponseEntity<OrdemDeServicoDTO> criarOrdemDeServico(@RequestBody OrdemDeServicoDTO ordemDeServicoDTO) {
        OrdemDeServicoDTO novaOS = ordemDeServicoService.criarOrdemDeServico(ordemDeServicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaOS);
    }

    // ... (dentro da classe OrdemDeServicoController)

    // --- NOVO ENDPOINT: Listar Todas ---
    // Responde a: GET /ordem-de-servico
    @GetMapping
    public ResponseEntity<List<OrdemDeServicoDTO>> listarTodasOS() {
        List<OrdemDeServicoDTO> lista = ordemDeServicoService.listarTodasOS();
        return ResponseEntity.ok(lista); // Retorna 200 OK
    }

    // --- NOVO ENDPOINT: Buscar por ID ---
    // Responde a: GET /ordem-de-servico/1 (ou /2, /3, etc.)
    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServicoDTO> buscarOSPorId(@PathVariable Long id) {
        OrdemDeServicoDTO osDTO = ordemDeServicoService.buscarOSPorId(id);
        return ResponseEntity.ok(osDTO); // Retorna 200 OK
    }

    // ... (dentro da classe OrdemDeServicoController)

    // --- NOVO ENDPOINT: Atualizar Status ---
    // Responde a: PUT /ordem-de-servico/1/status
    @PutMapping("/{id}/status")
    public ResponseEntity<OrdemDeServicoDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateDTO statusUpdateDTO) {

        OrdemDeServicoDTO osAtualizada = ordemDeServicoService.atualizarStatus(id, statusUpdateDTO);
        return ResponseEntity.ok(osAtualizada); // Retorna 200 OK com a OS atualizada
    }
    // ... (dentro da classe OrdemDeServicoController)

    // --- NOVO ENDPOINT: Monitoramento (Tempo Médio) ---
    // Responde a: GET /ordem-de-servico/monitoramento/tempo-medio
    @GetMapping("/monitoramento/tempo-medio")
    public ResponseEntity<String> getTempoMedio() {
        String tempoMedio = ordemDeServicoService.getTempoMedioExecucao();
        return ResponseEntity.ok(tempoMedio);
    }
}