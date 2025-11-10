package br.com.apioficina.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PecaDTO {

    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
}
