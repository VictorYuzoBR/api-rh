package com.rh.api_rh.comunicado;

import com.rh.api_rh.DTO.aplicacao.comunicado.alterarVisto_dto;
import com.rh.api_rh.DTO.aplicacao.comunicado.enviarComunicado_dto;
import com.rh.api_rh.comunicado.comunicado_funcionario.comunicado_funcionario_model;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comunicado")
public class comunicado_controller {

    private final comunicado_service comunicadoservice;

    @PostMapping
    public ResponseEntity<?> enviarComunicado(@RequestBody enviarComunicado_dto dto) {

        try {
            comunicado_model res = comunicadoservice.enviar(dto);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.ok(res);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/funcionario/{id}")
    public ResponseEntity<List<comunicado_funcionario_model>> buscarComunicadosFuncionario(@PathVariable UUID id) {

        try {

            List<comunicado_funcionario_model> res =  comunicadoservice.buscarComunicadosFuncionario(id);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.ok(res);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<comunicado_model> buscarComunicado(@PathVariable Long id) {

        try {
            comunicado_model res =  comunicadoservice.buscarComunicado(id);
            if (res != null) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/alterarVisto")
    public ResponseEntity<comunicado_funcionario_model> alterarVisto(@RequestBody alterarVisto_dto dto) {

        try {
            comunicado_funcionario_model res =  comunicadoservice.alterarVisto(dto.getIdcomunicadotabelaaxuiliar());
            if (res != null) {
                return ResponseEntity.ok(res);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping
    public  ResponseEntity<List<comunicado_model>> buscarComunicados() {
        try {
            List<comunicado_model> res = comunicadoservice.listarComunicados();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
