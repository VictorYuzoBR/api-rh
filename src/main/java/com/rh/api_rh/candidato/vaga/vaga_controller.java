package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.cadastrarVaga_dto;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor()
@RestController
@RequestMapping("/vaga")
public class vaga_controller {

    private final vaga_service vagaservice;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastrarVaga_dto dto) {

        try {

            String res = vagaservice.cadastrar(dto);
            if (res.equals("sucesso")) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }

        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<vaga_model>> listar() {

        try {
            List<vaga_model> res = vagaservice.listar();
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }   catch (Exception e) {
            return null;
        }

    }

    @GetMapping("/intermediaria")
    public  ResponseEntity<List<vaga_habilidade_model>> listarIntermediaria() {

        try {

            List<vaga_habilidade_model> res = vagaservice.listarTabelaIntermediaria();
            if (res != null) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }

        } catch (Exception e) {
            return null;
        }

    }


}
