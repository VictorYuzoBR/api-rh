package com.rh.api_rh.candidato.habilidade;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
