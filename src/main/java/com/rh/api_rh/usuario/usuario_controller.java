package com.rh.api_rh.usuario;

import com.rh.api_rh.DTO.trocasenha_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class usuario_controller {

    private final usuario_service usuario_service;


    @GetMapping
    public ResponseEntity<List<usuario_model>> listar() {
        try {
            return ResponseEntity.ok(usuario_service.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /// RECEBE ID DO USUARIO E NOVA SENHA
    @PutMapping("/novasenha")
    public ResponseEntity<String> trocarsenha(@RequestBody trocasenha_dto dto) {

        return ResponseEntity.ok(usuario_service.trocasenha(dto.getSenha(), dto.getId()));

    }

}
