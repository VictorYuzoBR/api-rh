package com.rh.api_rh.usuario;

import com.rh.api_rh.DTO.login.trocasenha_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/usuario")
public class usuario_controller {

    private final usuario_service usuario_service;
    private final log_repository log_repository;
    private final funcionario_repository funcionario_repository;


    @GetMapping
    public ResponseEntity<List<usuario_model>> listar() {
        try {
            return ResponseEntity.ok(usuario_service.listar());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }



    @PutMapping("/novasenha")
    public ResponseEntity<String> trocarsenha(@RequestBody trocasenha_dto dto) {

        Optional<funcionario_model> funcionario = funcionario_repository.findByEmail(dto.getEmail());
        if (funcionario.isPresent()) {
            UUID id = funcionario.get().getIdusuario().getId();
            String res = usuario_service.trocasenha(dto.getSenha(), id);
            if (res.equals("A senha foi atualizada com sucesso!")) {


                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }
        } else {
            return ResponseEntity.badRequest().body("Algo deu errado");
        }




    }

}
