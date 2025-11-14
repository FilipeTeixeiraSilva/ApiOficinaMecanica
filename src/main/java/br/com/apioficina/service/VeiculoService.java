package br.com.apioficina.service;

import br.com.apioficina.dto.VeiculoDTO;
import br.com.apioficina.model.ClienteEntity;
import br.com.apioficina.model.VeiculoEntity;
import br.com.apioficina.repository.ClienteRepository;
import br.com.apioficina.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VeiculoService {
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public VeiculoDTO criarVeiculo(VeiculoDTO veiculoDTO){
        VeiculoEntity veiculoEntity = new VeiculoEntity();
        veiculoEntity.setPlaca(veiculoDTO.getPlaca());
        veiculoEntity.setMarca(veiculoDTO.getMarca());
        veiculoEntity.setModelo(veiculoDTO.getModelo());
        veiculoEntity.setAno(veiculoDTO.getAno());
        veiculoEntity.setCliente(clienteRepository.findById(veiculoDTO.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + veiculoDTO.getClienteId())));

        VeiculoEntity veiculoSalvo = veiculoRepository.save(veiculoEntity);

        VeiculoDTO veiculoDTOSalvo = new VeiculoDTO();
        veiculoDTOSalvo.setPlaca(veiculoSalvo.getPlaca());
        veiculoDTOSalvo.setMarca(veiculoSalvo.getMarca());
        veiculoDTOSalvo.setModelo(veiculoSalvo.getModelo());
        veiculoDTOSalvo.setAno(veiculoSalvo.getAno());
        veiculoDTOSalvo.setClienteId(veiculoSalvo.getCliente().getId());

        return veiculoDTOSalvo;

    }
}
