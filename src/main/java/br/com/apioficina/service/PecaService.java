package br.com.apioficina.service;

import br.com.apioficina.dto.PecaDTO;
import br.com.apioficina.model.PecaEntity;
import br.com.apioficina.repository.PecaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PecaService {
    @Autowired
    private PecaRepository pecaRepository;

    public PecaDTO criarPeca(PecaDTO pecaDTO){
        PecaEntity pecaEntity = new PecaEntity();
        pecaEntity.setDescricao(pecaDTO.getDescricao());
        pecaEntity.setPreco(pecaDTO.getPreco());
        pecaEntity.setQuantidadeEstoque(pecaDTO.getQuantidadeEstoque());

        PecaEntity pecaSalvo = pecaRepository.save(pecaEntity);

        PecaDTO pecaDTOSalvo = new PecaDTO();
        pecaDTOSalvo.setDescricao(pecaSalvo.getDescricao());
        pecaDTOSalvo.setPreco(pecaSalvo.getPreco());
        pecaDTOSalvo.setQuantidadeEstoque(pecaSalvo.getQuantidadeEstoque());

        return pecaDTOSalvo;
    }
}
