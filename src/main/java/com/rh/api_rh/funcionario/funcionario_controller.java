package com.rh.api_rh.funcionario;


import com.rh.api_rh.DTO.aplicacao.funcionario.buscarParaEnviarComunicadoFuncao_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.buscarParaEnviarComunicadoNome_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.buscarParaEnviarComunicadoSetor_dto;
import com.rh.api_rh.DTO.login.aceitartermo_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.atualizarfuncionario_dto;
import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import com.rh.api_rh.DTO.cadastro.emailnotificarcadastro_dto;
import com.rh.api_rh.funcionario.endereco.endereco_mapper;
import com.rh.api_rh.funcionario.endereco.endereco_service;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.funcionario.telefone.telefone_mapper;
import com.rh.api_rh.funcionario.telefone.telefone_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import com.rh.api_rh.util.email_service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    private final funcionario_repository funcionario_repository;
    private final token_service token_service;



    @PostMapping
    public ResponseEntity<funcionario_model> cadastrar(@RequestBody @Valid cadastroFuncionario_dto dto) {


        emailnotificarcadastro_dto dados  = funcionario_mapper.convert(dto);
        funcionario_model funcionario = dados.getFuncionario();
        usuarioprovisorio provisorio = dados.getProvisorio();

        try {
            funcionario_model result = funcionario_service.cadastrar(funcionario);
            if (result != null) {
                email_service.enviarcadastro(funcionario.getEmail(), provisorio);

                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().build();
            }


        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

    @GetMapping("/{id}")
    public ResponseEntity<funcionario_model> perfil(@PathVariable UUID id) {
        try {
            funcionario_model funcionario = funcionario_service.buscar(id);
            if (funcionario != null) {
                return ResponseEntity.ok(funcionario);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/buscarporcargo/{cargo}")
    public ResponseEntity<List<funcionario_model>> buscarporcargo(@PathVariable Cargo cargo) {
        try {
            List<funcionario_model> funcionarios = funcionario_repository.findByCargo(cargo);
            return ResponseEntity.ok(funcionarios);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable UUID id) {

        try {
            String res = funcionario_service.excluir(id);
            if  (res.equals("Excluido com sucesso!")) {
                return ResponseEntity.ok().body("Excluido com sucesso!");
            } else {
                return ResponseEntity.badRequest().body(res);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody @Valid atualizarfuncionario_dto dto, HttpServletRequest request) {

        try {
            UUID idrh = UUID.fromString(token_service.returnIdRh(request));

            String res = funcionario_service.atualizar(dto, idrh);
            if (res.equals("Atualizado com sucesso!")) {
                return ResponseEntity.ok().body("Atualizado com sucesso!");
            } else {
                return ResponseEntity.badRequest().body(res);
            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PutMapping("/aceitartermo")
    public ResponseEntity<String> aceitartermo(@RequestBody aceitartermo_dto dto) {

        try {
            String res = funcionario_service.aceitarTermo(dto);
            if (res.equals("Atualizado com sucesso!")) {
                return ResponseEntity.ok().body("Atualizado com sucesso!");
            }
             else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }

    @GetMapping("/generateadmin")
    public ResponseEntity<String> generateadmin() {

        String res = funcionario_service.generateadmin();
        return ResponseEntity.ok().body(res);

    }

    @PostMapping("/buscarParaEnviarComunicadoSetor")
    public ResponseEntity<List<funcionario_model>>  buscarParaEnviarComunicadoSetor(@RequestBody buscarParaEnviarComunicadoSetor_dto dto) {

        try {

            List<funcionario_model> result = funcionario_service.listarPorSetorRecebendoLista(dto.getIdsetores());
            if (result != null) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/buscarParaEnviarComunicadoFuncao")
    public ResponseEntity<List<funcionario_model>>  buscarParaEnviarComunicadoFuncao(@RequestBody buscarParaEnviarComunicadoFuncao_dto dto) {

        try {

            List<funcionario_model> result = funcionario_service.listarPorFuncaoRecebendoLista(dto.getFuncoes());
            if (result != null) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/buscarParaEnviarComunicadoNome")
    public ResponseEntity<List<funcionario_model>>  buscarParaEnviarComunicadoNome(@RequestBody buscarParaEnviarComunicadoNome_dto dto) {

        try {

            List<funcionario_model> result = funcionario_service.listarPorNomeRecebendoLista(dto.getNomes());
            if (result != null) {
                return ResponseEntity.ok().body(result);
            } else {
                return ResponseEntity.badRequest().body(null);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }





}
