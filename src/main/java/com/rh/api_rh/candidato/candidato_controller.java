package com.rh.api_rh.candidato;

import com.rh.api_rh.DTO.aplicacao.candidato.atualizarCandidato_dto;
import com.rh.api_rh.DTO.aplicacao.candidato.buscarComBaseHabilidades_dto;
import com.rh.api_rh.DTO.aplicacao.candidato.enviarEmailNovaVaga_dto;
import com.rh.api_rh.DTO.aplicacao.candidato.retornarPerfil_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidato_dto;
import com.rh.api_rh.DTO.login.trocaSenhaCandidato_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_service;
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
    private final candidato_vaga_service candidato_vaga_service;

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

    @PostMapping("/aceitartermo/{id}")
    public ResponseEntity<String> aceitartermo(@PathVariable Long id){
        try {
            String res = candidato_service.aceitartermo(id);
            if (res == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }   catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<retornarPerfil_dto>  perfil(@PathVariable Long id){

        try {
            retornarPerfil_dto dto =  candidato_service.perfil(id);
            if (dto == null){
                return ResponseEntity.badRequest().body(null);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(dto);
            }

        }    catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/atualizar")
    public ResponseEntity<candidato_model> atualizar(@RequestBody atualizarCandidato_dto dto){

        try {
            candidato_model res =  candidato_service.atualizar(dto);
            if (res == null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }   catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>  excluir(@PathVariable Long id){

        try {
            String res = candidato_service.excluir(id);
            if (res.equals("excluido com sucesso")) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/buscarCandidaturas/{id}")
    public ResponseEntity<?> buscarCandidaturas(@PathVariable Long id){

        try {

            List<candidato_vaga_model> res = candidato_service.buscarCandidaturasUsuario(id);
            if  (res == null){
                return ResponseEntity.badRequest().body("aaa");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/trocarSenhaCandidato")
    public ResponseEntity<String> trocarSenha(@RequestBody trocaSenhaCandidato_dto dto){

        try {
            String res = candidato_service.trocarSenha(dto);
            if (res.equals("sucesso")) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }  else {
                return ResponseEntity.badRequest().body(res);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }




}
