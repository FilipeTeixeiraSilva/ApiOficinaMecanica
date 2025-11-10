package br.com.apioficina.service;

import br.com.apioficina.dto.ClienteDTO;
import br.com.apioficina.model.ClienteEntity;
import br.com.apioficina.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteDTO criarCliente(ClienteDTO clienteDTO) {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNome(clienteDTO.getNome());
        clienteEntity.setCpf(clienteDTO.getCpf());
        clienteEntity.setTelefone(clienteDTO.getTelefone());
        clienteEntity.setEmail(clienteDTO.getEmail());

        ClienteEntity clienteSalvo = clienteRepository.save(clienteEntity);

        ClienteDTO clienteDTOSalvo = new ClienteDTO();
        clienteDTOSalvo.setNome(clienteSalvo.getNome());
        clienteDTOSalvo.setCpf(clienteSalvo.getCpf());
        clienteDTOSalvo.setTelefone(clienteSalvo.getTelefone());
        clienteDTOSalvo.setEmail(clienteSalvo.getEmail());

        return clienteDTOSalvo;
    }
}
