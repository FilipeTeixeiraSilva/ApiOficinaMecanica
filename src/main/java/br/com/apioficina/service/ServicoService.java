package br.com.apioficina.service;

import br.com.apioficina.dto.ServicoDTO;
import br.com.apioficina.model.ServicoEntity;
import br.com.apioficina.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {
    @Autowired
    private ServicoRepository servicoRepository;

    public ServicoDTO criarServico(ServicoDTO servicoDTO){
        ServicoEntity servicoEntity = new ServicoEntity();
        servicoEntity.setDescricao(servicoDTO.getDescricao());
        servicoEntity.setPreco(servicoDTO.getPreco());

        ServicoEntity servicoSalvo = servicoRepository.save(servicoEntity);

        ServicoDTO servicoDTOSalvo = new ServicoDTO();
        servicoDTOSalvo.setDescricao(servicoSalvo.getDescricao());
        servicoDTOSalvo.setPreco(servicoSalvo.getPreco());

        return servicoDTOSalvo;
    }
}
