package com.rh.api_rh.setor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rh.api_rh.DTO.cadastro.cadastroSetor_dto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setor")
public class setor_controller {

    @Autowired
    private setor_service setor_service;
    @Autowired
    private setor_mapper setor_mapper;
    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping
    public ResponseEntity<List<setor_model>> listar() {
        try {
            return ResponseEntity.ok(setor_service.listar());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<setor_model> cadastrar(@RequestBody @Valid cadastroSetor_dto setordto) {

        setor_model setor = setor_mapper.convert(setordto);

        try {
            setor_model result = setor_service.cadastrar(setor);
            if (result != null) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<setor_model> buscarsetor(@PathVariable int id) {

        Long longid = Long.valueOf(id);

        try {
            return ResponseEntity.ok(setor_service.pesquisa(longid));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarsetor(@PathVariable Long id) {

        try {

            String res =  setor_service.deletar(id);
            if (res.equals("Deletado com sucesso!")) {
                return ResponseEntity.ok("Deletado com sucesso!");
            }  else {
                return ResponseEntity.badRequest().body(res);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




}
