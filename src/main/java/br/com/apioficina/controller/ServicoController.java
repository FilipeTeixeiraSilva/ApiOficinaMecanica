package br.com.apioficina.controller;

import br.com.apioficina.dto.ServicoDTO;
import br.com.apioficina.service.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servico")
public class ServicoController {
    @Autowired
    private ServicoService servicoService;

    @PostMapping
    public ResponseEntity<ServicoDTO> criarServico(@RequestBody ServicoDTO servicoDTO){
        ServicoDTO servicoDTOSalvo = servicoService.criarServico(servicoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoDTOSalvo);
    }
}
