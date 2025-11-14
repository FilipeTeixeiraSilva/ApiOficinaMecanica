package br.com.apioficina.service;

import br.com.apioficina.dto.UsuarioDTO;
import br.com.apioficina.model.UsuarioEntity;
import br.com.apioficina.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO){
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setUsuario(usuarioDTO.getUsuario());
        usuarioEntity.setCpf(usuarioDTO.getCpf());
        usuarioEntity.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        UsuarioEntity usuarioEntitySalvo = usuarioRepository.save(usuarioEntity);

        UsuarioDTO usuarioDTOSalvo = new UsuarioDTO();
        usuarioDTOSalvo.setUsuario(usuarioEntitySalvo.getUsuario());
        usuarioDTOSalvo.setCpf(usuarioEntitySalvo.getCpf());
        usuarioDTOSalvo.setSenha("senha cadastrada com sucesso");

        return usuarioDTOSalvo;
    }

}
