package br.com.apioficina.dto; // Ou o seu pacote dto

import br.com.apioficina.model.enums.StatusOS;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdemDeServicoDTO {

    // --- 1. CAMPOS DE ENTRADA (O que vem do JSON) ---
    // (O erro anterior aconteceu porque estes campos faltavam)
    private Long clienteId;
    private Long veiculoId;
    private List<Long> servicosIds;
    private List<Long> pecasIds;

    // --- 2. CAMPOS DE SAÍDA (O que vai na Resposta) ---
    private Long id;
    private StatusOS status;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private BigDecimal valorTotal;

    // Detalhes extras na resposta
    private String clienteNome;
    private String veiculoPlaca;
    private List<ServicoDTO> servicos; // Usamos os DTOs que já existem
    private List<PecaDTO> pecas; // Usamos os DTOs que já existem
}