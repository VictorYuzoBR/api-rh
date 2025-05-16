package com.rh.api_rh.codigotrocasenha;

import com.rh.api_rh.DTO.criacodigotrocasenha_dto;
import com.rh.api_rh.DTO.validarcodigotrocasenha_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.usuario.usuario_model;
import com.rh.api_rh.usuario.usuario_repository;
import com.rh.api_rh.usuario.usuario_service;
import com.rh.api_rh.util.email_service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/codigosenha")
public class codigotrocasenha_controller {

    final codigotrocasenha_service codigotrocasenha_service;

    final com.rh.api_rh.util.email_service email_service;

    final com.rh.api_rh.usuario.usuario_service usuario_service;

    final usuario_repository usuario_repo;

    final funcionario_repository funcionario_repo;


    @PostMapping
    public ResponseEntity<String> enviaremail(@RequestBody criacodigotrocasenha_dto dto) {

       Optional<funcionario_model> funcionario = funcionario_repo.findByEmail(dto.getEmail());
       if (funcionario.isPresent()) {
           UUID iduser = funcionario.get().getIdusuario().getId();
           return ResponseEntity.ok(email_service.enviarcodigosenha(dto.getEmail(), iduser));
       } else {
           return ResponseEntity.badRequest().body("Nenhuma conta associada a este email foi detectada em nosso sistema");
       }



    }

    /// Lista codigos existentes.criado apenas para testes
    @GetMapping
    public ResponseEntity<List<codigotrocasenha_model>> listar() {
        return ResponseEntity.ok(codigotrocasenha_service.listar());
    }


    //todo trocar id usuario por id funcionario
    ///ROTA QUE FAZ VALIDAÇÃO DO CÓDIGO, PRIMEIRO VALIDA O CODIGO E DEPOIS TEMPO DE EXPIRAÇÃO, RECEBE UM DTO COM ID DO USUARIO E O CODIGO DIGITADO
    @PostMapping("/validar")
    public ResponseEntity<String> validarcodigo(@RequestBody validarcodigotrocasenha_dto dto) {

        Optional<funcionario_model> funcionario = funcionario_repo.findByEmail(dto.getEmail());
        if (funcionario.isPresent()) {
            UUID id = funcionario.get().getIdusuario().getId();
            String codigo = dto.getCodigo();
            codigotrocasenha_model entidade = codigotrocasenha_service.validarcodigo(id,codigo);
            if (entidade != null) {
                if(codigotrocasenha_service.validartempo(entidade)) {
                    return ResponseEntity.ok("Você pode trocar sua senha");
                } else {
                    codigotrocasenha_service.deletar(entidade.getIdusuario());
                    return ResponseEntity.badRequest().body("Código expirado");
                }
            } else {
                return ResponseEntity.badRequest().body("Código inexistente");
            }
        } else {
            return ResponseEntity.badRequest().build();
        }



    }



}
