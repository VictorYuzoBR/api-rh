package com.rh.api_rh.testes;


import com.rh.api_rh.DTO.criacodigotrocasenha_dto;
import com.rh.api_rh.DTO.trocasenha_dto;
import com.rh.api_rh.codigotrocasenha.codigotrocasenha_model;
import com.rh.api_rh.codigotrocasenha.codigotrocasenha_service;
import com.rh.api_rh.usuario.usuario_service;
import com.rh.api_rh.util.email_service;
import com.rh.api_rh.util.registro_service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teste")
public class testes_controller {

    final codigotrocasenha_service codigotrocasenha_service;

    final email_service email_service;

    final usuario_service usuario_service;

    @PostMapping("/enviar")
    public ResponseEntity teste() {
        String res = email_service.enviarcodigosenha();

        return ResponseEntity.ok(res);

    }

    @PostMapping
    public ResponseEntity<String> cadastro(@RequestBody criacodigotrocasenha_dto dto) {

        return ResponseEntity.ok(codigotrocasenha_service.cadastrar(dto.getId()));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable UUID id) {
        codigotrocasenha_service.deletar(id);
        return ResponseEntity.ok("Deletado com sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<codigotrocasenha_model>> listar() {
        return ResponseEntity.ok(codigotrocasenha_service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<codigotrocasenha_model> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(codigotrocasenha_service.buscar(id));
    }

    @PutMapping
    public ResponseEntity<String> trocarsenha(@RequestBody trocasenha_dto dto) {

        return ResponseEntity.ok(usuario_service.trocasenha(dto.getSenha(), dto.getId()));

    }

}
