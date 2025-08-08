package com.rh.api_rh.funcionario.telefone;

import com.rh.api_rh.DTO.cadastro_dto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/telefone")
public class telefone_controller {

    final telefone_service telefone_service;
    final telefone_mapper telefone_mapper;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastro_dto dto) {

        telefone_model telefone = telefone_mapper.convert(dto);

        try {
            return new ResponseEntity<>(telefone_service.cadastrar(telefone), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<List<telefone_model>> listar() {

        try {
            return new ResponseEntity<>(telefone_service.listar(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
