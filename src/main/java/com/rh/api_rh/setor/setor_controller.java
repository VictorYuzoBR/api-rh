package com.rh.api_rh.setor;

import com.rh.api_rh.DTO.cadastro.cadastroSetor_dto;
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


    @GetMapping
    public ResponseEntity<List<setor_model>> listar() {
        try {
            return ResponseEntity.ok(setor_service.listar());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastroSetor_dto setordto) {

        setor_model setor = setor_mapper.convert(setordto);

        try {
            return ResponseEntity.ok(setor_service.cadastrar(setor));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<setor_model> buscarsetor(@PathVariable int id) {

        Long longid = Long.valueOf(id);

        try {
            return ResponseEntity.ok(setor_service.pesquisa(longid));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
