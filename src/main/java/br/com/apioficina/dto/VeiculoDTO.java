package br.com.apioficina.dto;

import lombok.Data;

@Data
public class VeiculoDTO {
    private String placa;
    private String marca;
    private String modelo;
    private Integer ano;
    private Long clienteId;
}
