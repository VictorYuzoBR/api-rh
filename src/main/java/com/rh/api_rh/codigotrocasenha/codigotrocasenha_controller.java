package com.rh.api_rh.codigotrocasenha;

import com.rh.api_rh.DTO.criacodigotrocasenha_dto;
import com.rh.api_rh.DTO.validarcodigotrocasenha_dto;
import com.rh.api_rh.usuario.usuario_service;
import com.rh.api_rh.util.email_service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/codigosenha")
public class codigotrocasenha_controller {

    final codigotrocasenha_service codigotrocasenha_service;

    final com.rh.api_rh.util.email_service email_service;

    final com.rh.api_rh.usuario.usuario_service usuario_service;


    //todo trocar id usuario por id funcionario
    ///  RECEBE EMAIL E ID DO USUARIO PARA CRIAR, CADASTRAR E ENVIAR CODIGO
    @PostMapping
    public ResponseEntity<String> enviaremail(@RequestBody criacodigotrocasenha_dto dto) {

        return ResponseEntity.ok(email_service.enviarcodigosenha(dto.getEmail(), dto.getId()));

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

        UUID id = dto.getId();
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

    }



}
