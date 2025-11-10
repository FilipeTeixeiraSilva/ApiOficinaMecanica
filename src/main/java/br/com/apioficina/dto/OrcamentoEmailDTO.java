package br.com.apioficina.dto; // Ou seu pacote dto

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

// DTO leve para a mensagem do Kafka (simulando o e-mail)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrcamentoEmailDTO {
    private Long osId;
    private String nomeCliente;
    private String emailCliente;
    private String placaVeiculo;
    private BigDecimal valorTotal;
}