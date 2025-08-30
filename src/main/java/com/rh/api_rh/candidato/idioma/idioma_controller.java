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
    public ResponseEntity<?> cadastrar(@RequestBody cadastroIdioma_dto dto) {
        idioma_model idioma = new idioma_model();
        try {
           idioma =  idiomaservice.cadastrar(dto);
           if (idioma == null) {
               return ResponseEntity.badRequest().body("idioma ja estava cadastrado");
           } else {
               return ResponseEntity.ok().body(idioma);
           }
        } catch (Exception e) {
            throw e;
        }


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
