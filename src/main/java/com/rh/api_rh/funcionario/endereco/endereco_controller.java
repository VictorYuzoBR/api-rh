package com.rh.api_rh.funcionario.endereco;


import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class endereco_controller {

    @Autowired
    endereco_service endereco_service;
    @Autowired
    endereco_mapper endereco_mapper;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastroFuncionario_dto dto) {

        endereco_model endereco = endereco_mapper.convert(dto);

        try {
            return new ResponseEntity<>(endereco_service.cadastrar(endereco), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<List<endereco_model>> listar() {

        try {
            return new ResponseEntity<>(endereco_service.listar(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

    }

    //todo criar update do endereco

}
