package com.rh.api_rh.candidato.codigotrocasenhaCandidato;

import com.rh.api_rh.DTO.login.criacodigotrocasenhacandidato_dto;
import com.rh.api_rh.DTO.login.validarcodigotrocasenhaCandidato_dto;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.candidato.candidato_service;
import com.rh.api_rh.util.email_service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/codigosenhacandidato")
@RequiredArgsConstructor
public class codigotrocasenhaCandidato_controller {

    private final codigotrocasenhaCandidato_service codigotrocasenhaCandidatoService;
    private final email_service  emailService;
    private final candidato_service candidatoService;

    @PostMapping
    public ResponseEntity<?> enviaremail(@RequestBody criacodigotrocasenhacandidato_dto dto) {

        try {

            String codigo = codigotrocasenhaCandidatoService.cadastrarcodigo(dto.getEmail());
            if (codigo.equals("candidato não encontrado") || codigo.equals("erro")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nenhum candidato com este email foi encontrado");
            } else {

                String res = emailService.enviarcodigosenhacandidato(dto.getEmail(), codigo);
                if (res.equals("Email enviado com sucesso!")) {
                    return ResponseEntity.status(HttpStatus.OK).body(dto.getEmail()); ///devolve o email para ser usado na proxima etapa
                }  else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

        }  catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao enviar email");
        }

    }

    @PostMapping("/validar")
    public ResponseEntity<String> validarcodigo(@RequestBody validarcodigotrocasenhaCandidato_dto dto) {

        try {
            return codigotrocasenhaCandidatoService.validarTudo(dto);
        }   catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao validar");
        }

    }



}
