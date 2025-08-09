package com.rh.api_rh.candidato.idioma;

import com.rh.api_rh.DTO.cadastro.cadastroIdioma_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/idioma")
@RestController
@RequiredArgsConstructor
public class idioma_controller {

    private final idioma_service idiomaservice;

    @PostMapping
    public ResponseEntity<idioma_model> cadastrar(@RequestBody cadastroIdioma_dto dto) {
        idioma_model idioma = new idioma_model();
        try {
           idioma =  idiomaservice.cadastrar(dto);
        } catch (Exception e) {
            throw e;
        }
        return ResponseEntity.ok().body(idioma);

    }

    @GetMapping
    public ResponseEntity<List<idioma_model>> listar() {
        try {
            return ResponseEntity.ok(idiomaservice.listar());
        }  catch (Exception e) {
            throw e;
        }
    }

}
