package com.rh.api_rh.funcionario;


import com.rh.api_rh.DTO.cadastro_dto;
import com.rh.api_rh.endereco.endereco_mapper;
import com.rh.api_rh.endereco.endereco_model;
import com.rh.api_rh.endereco.endereco_service;
import com.rh.api_rh.telefone.telefone_mapper;
import com.rh.api_rh.telefone.telefone_model;
import com.rh.api_rh.telefone.telefone_service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/funcionario")
public class funcionario_controller {

    private final funcionario_service funcionario_service;
    private final funcionario_mapper funcionario_mapper;
    private final endereco_service endereco_service;
    private final telefone_service telefone_service;
    private final telefone_mapper telefone_mapper;
    private final endereco_mapper endereco_mapper;



    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastro_dto dto) {


        funcionario_model funcionario = funcionario_mapper.convert(dto);

        try {
            return new ResponseEntity<>(funcionario_service.cadastrar(funcionario), HttpStatus.CREATED);
        } catch (Exception e) {


            return new ResponseEntity<>("Cadastrado com sucesso", HttpStatus.BAD_REQUEST);
        }



    }

    @GetMapping
    public ResponseEntity<List<funcionario_model>> listar() {
        try {
            return ResponseEntity.ok(funcionario_service.listar());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable UUID id) {

        funcionario_model funcionario = funcionario_service.buscar(id);
        try {
            return new ResponseEntity<>(funcionario_service.excluir(funcionario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
