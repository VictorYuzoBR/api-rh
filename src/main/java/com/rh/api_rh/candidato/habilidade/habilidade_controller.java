package com.rh.api_rh.candidato.habilidade;

import com.rh.api_rh.DTO.cadastroHabilidade_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habilidade")
@RequiredArgsConstructor
public class habilidade_controller {

    private final habilidade_service habilidadeService;

    @GetMapping
    public ResponseEntity<List<habilidade_model>> listar() {

        try {
            List<habilidade_model> habilidades = habilidadeService.listar();
            return  ResponseEntity.status(HttpStatus.OK).body(habilidades);
        }    catch (Exception e) {
            return null;
        }

    }

    @PostMapping
    public  ResponseEntity<habilidade_model> cadastrar(@RequestBody cadastroHabilidade_dto dto) {
        habilidade_model habilidade = new habilidade_model();
        try {
           habilidade = habilidadeService.cadastrarHabilidade(dto);
        } catch (Exception e) {
            return null;
        }
        return ResponseEntity.status(HttpStatus.OK).body(habilidade);
    }

}
