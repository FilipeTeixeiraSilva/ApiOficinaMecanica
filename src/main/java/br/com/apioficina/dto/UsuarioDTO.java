package br.com.apioficina.dto;

import br.com.apioficina.model.enums.TipoUsuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private TipoUsuario usuario;
    private String cpf;
    private String senha;
}
