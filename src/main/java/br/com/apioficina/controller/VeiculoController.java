package br.com.apioficina.controller;

import br.com.apioficina.dto.ClienteDTO;
import br.com.apioficina.dto.VeiculoDTO;
import br.com.apioficina.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/veiculo")
public class VeiculoController {
    @Autowired
    private VeiculoService veiculoService;
    @PostMapping
    public ResponseEntity<VeiculoDTO>criarVeiculo(@RequestBody VeiculoDTO veiculoDTO){
        VeiculoDTO veiculoDTOSalvo = veiculoService.criarVeiculo(veiculoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculoDTOSalvo);
    }
}
