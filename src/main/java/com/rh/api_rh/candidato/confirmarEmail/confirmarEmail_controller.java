package com.rh.api_rh.candidato.confirmarEmail;

import com.rh.api_rh.DTO.cadastro.confirmarEmail_dto;
import com.rh.api_rh.DTO.cadastro.validarCodigoConfirmacao_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirmaremail")
@RequiredArgsConstructor
public class confirmarEmail_controller {

    private final confirmarEmail_service service;

    @PostMapping
    public ResponseEntity<String> enviarEmail(@RequestBody confirmarEmail_dto dto) {

        try {

            String res = service.enviarEmail(dto);
            if (res.equals("email enviado com sucesso!")) {
                return ResponseEntity.ok().body(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }

        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao enviar o e-mail!");
        }

    }

    @PostMapping("/validar") ResponseEntity<String> confirmarCodigo(@RequestBody validarCodigoConfirmacao_dto dto) {

        try {

            String res = service.validarCodigoConfirmacaoEmail(dto);
            if (res.equals("email confirmado com sucesso!")) {
                return ResponseEntity.ok().body(res);
            } else {
                return  ResponseEntity.badRequest().body(res);
            }

        } catch (Exception ex) {
            return  ResponseEntity.internalServerError().body("Erro ao confirmar o e-mail!");
        }

    }


}
