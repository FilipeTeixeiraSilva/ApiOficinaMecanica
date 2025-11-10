package br.com.apioficina.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServicoDTO {

    private String descricao;
    private BigDecimal preco;
}
