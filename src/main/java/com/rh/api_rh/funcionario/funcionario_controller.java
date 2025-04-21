package com.rh.api_rh.funcionario;


import com.rh.api_rh.DTO.cadastro_dto;
import com.rh.api_rh.DTO.emailnotificarcadastro_dto;
import com.rh.api_rh.endereco.endereco_mapper;
import com.rh.api_rh.endereco.endereco_model;
import com.rh.api_rh.endereco.endereco_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.telefone.telefone_mapper;
import com.rh.api_rh.telefone.telefone_model;
import com.rh.api_rh.telefone.telefone_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import com.rh.api_rh.util.email_service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    private final email_service email_service;
    private final log_repository log_repository;



    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastro_dto dto) {


        emailnotificarcadastro_dto dados  = funcionario_mapper.convert(dto);
        funcionario_model funcionario = dados.getFuncionario();
        usuarioprovisorio provisorio = dados.getProvisorio();

        try {
            String resposta = funcionario_service.cadastrar(funcionario);
            if (resposta.equals("Cadastrado com sucesso!")) {
                email_service.enviarcadastro(funcionario.getEmail(), provisorio);

                log_model log = new log_model();
                log.setRegistro(funcionario.getIdusuario().getRegistro());
                log.setAcao("Novo funcionário cadastrado no sistema com registro: "+funcionario.getIdusuario().getRegistro());
                log.setData(new Date());
                log_repository.save(log);

                return ResponseEntity.ok().body("Cadastrado com sucesso!");
            } else {
                return ResponseEntity.badRequest().body("Erro ao cadastrar funcionario!");
            }


        } catch (Exception e) {


            return new ResponseEntity<>("Erro ao cadastrar", HttpStatus.BAD_REQUEST);
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
