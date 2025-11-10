package br.com.apioficina.controller;

import br.com.apioficina.dto.PecaDTO;
import br.com.apioficina.service.PecaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/peca")
public class PecaController {
    @Autowired
    private PecaService pecaService;

    @PostMapping
    public ResponseEntity<PecaDTO> criarPeca(@RequestBody PecaDTO pecaDTO){
        PecaDTO pecaDTOSalvo = pecaService.criarPeca(pecaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pecaDTOSalvo);
    }
}
