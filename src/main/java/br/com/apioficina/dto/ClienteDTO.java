package br.com.apioficina.dto;

import lombok.Data;

@Data
public class ClienteDTO {

    private String nome;
    private String cpf;
    private String telefone;
    private String email;
}
