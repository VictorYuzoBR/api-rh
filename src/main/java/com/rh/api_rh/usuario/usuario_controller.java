package com.rh.api_rh.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class usuario_controller {

    private final usuario_service usuario_service;

    @PostMapping
    public ResponseEntity<usuario_model> cadastrar() {
        try {
            return ResponseEntity.ok(usuario_service.criar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<usuario_model>> listar() {
        try {
            return ResponseEntity.ok(usuario_service.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
