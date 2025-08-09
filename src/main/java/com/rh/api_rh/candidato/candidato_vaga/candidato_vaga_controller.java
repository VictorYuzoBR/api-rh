package com.rh.api_rh.candidato.candidato_vaga;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/candidatura")
@RestController
public class candidato_vaga_controller {

    private final candidato_vaga_service candidatovagaService;

    @GetMapping
    public ResponseEntity<List<candidato_vaga_model>> listar() {
        try {
            return ResponseEntity.ok().body(candidatovagaService.listar());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
