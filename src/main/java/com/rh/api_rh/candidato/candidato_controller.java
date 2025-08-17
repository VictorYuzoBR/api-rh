package com.rh.api_rh.candidato;

import com.rh.api_rh.DTO.aplicacao.candidato.buscarComBaseHabilidades_dto;
import com.rh.api_rh.DTO.aplicacao.candidato.enviarEmailNovaVaga_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidato_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_model;
import com.rh.api_rh.util.email_service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/candidato")
public class candidato_controller {

    private final candidato_service candidato_service;
    private final email_service email_service;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastroCandidato_dto dto){
        String res ="";
        try {
            res = candidato_service.cadastrar(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping
    public ResponseEntity<List<candidato_model>> listar(){

        List<candidato_model> res =  new ArrayList<>();

        try {
            res = candidato_service.listar();
        } catch (Exception e) {
            return null;
        }

        return ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @GetMapping("/habilidades/{id}")
    public ResponseEntity<List<candidato_habilidade_model>> listarHabilidades(@PathVariable  Long id){
        List<candidato_habilidade_model> res =  new ArrayList<>();
        try {
            res = candidato_service.listarhabilidades(id);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }  catch (Exception e) {
            return null;
        }

    }

    @GetMapping("/idiomas/{id}")
    public ResponseEntity<List<candidato_idioma_model>> listarIdiomas(@PathVariable  Long id){
        List<candidato_idioma_model> res =  new ArrayList<>();
        try {
            res = candidato_service.listaridiomas(id);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            return null;
        }
    }

    @PostMapping("/filtrar")
    public ResponseEntity<List<candidato_model>> filtrarPorHabilidades(@RequestBody buscarComBaseHabilidades_dto dto){


        try {

            List<candidato_model> res =  candidato_service.listarComBaseHabilidades(dto.getHabilidades());
            return ResponseEntity.status(HttpStatus.OK).body(res);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/enviarEmailNovaVaga")
    public ResponseEntity<String> enviarEmailNovaVaga(@RequestBody enviarEmailNovaVaga_dto dto){

        try {
            String res = candidato_service.enviarEmailParaCandidatosComHabilidade(dto);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }



}
