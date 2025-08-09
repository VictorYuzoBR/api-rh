package com.rh.api_rh.funcionario;


import com.rh.api_rh.DTO.login.aceitartermo_dto;
import com.rh.api_rh.DTO.aplicacao.funcionario.atualizarfuncionario_dto;
import com.rh.api_rh.DTO.cadastro.cadastroFuncionario_dto;
import com.rh.api_rh.DTO.cadastro.emailnotificarcadastro_dto;
import com.rh.api_rh.funcionario.endereco.endereco_mapper;
import com.rh.api_rh.funcionario.endereco.endereco_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.funcionario.telefone.telefone_mapper;
import com.rh.api_rh.funcionario.telefone.telefone_service;
import com.rh.api_rh.usuario.usuarioprovisorio;
import com.rh.api_rh.util.email_service;
import lombok.RequiredArgsConstructor;
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



    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastroFuncionario_dto dto) {


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
                return ResponseEntity.badRequest().body("Erro ao cadastrarParaCandidato funcionario!");
            }


        } catch (Exception e) {


            return new ResponseEntity<>("Erro ao cadastrarParaCandidato", HttpStatus.BAD_REQUEST);
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

        funcionario_model funcionario = funcionario_service.buscar(id);
        try {
            return new ResponseEntity<>(funcionario_service.excluir(funcionario), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    ///  o DTO recebe o id do funcionario RH que está atualizando o funcionário comum, os dados do funcionário comum serão puxados a partir do email
    /// então é necessário enviar no DTO os dados presentes no objeto atualizar funcionario_dto a partir da janela de perfil do funcionario comum que
    /// o usuário RH estará vendo
    /// exemplo: usuario RH marcio está no perfil do usuario adrian, enviar no dto o id do usuário marcio que estará no token, e pegar os dados dos campos do perfil do
    /// adrian e enviar no dto
    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody atualizarfuncionario_dto dto) {

        try {
            String registroDoFuncionarioComum = funcionario_service.atualizar(dto);

            Optional<funcionario_model> funcionariorh = funcionario_repository.findByEmail(dto.getEmailfuncionariorh());
            if (funcionariorh.isPresent()) {
                String registroDoRh = funcionariorh.get().getIdusuario().getRegistro();
                String texto = "O usuário RH de registro: " + registroDoRh + " realizou mudanças nas informações do funcionário de registro: " + registroDoFuncionarioComum;
                log_model log = new log_model();
                log.setAcao(texto);
                log.setRegistro(registroDoRh);
                log.setData(new Date());
                log_repository.save(log);


                return ResponseEntity.ok().body("Atualizado com sucesso!");
            } else {
                return ResponseEntity.badRequest().body("O email do funcionário RH está incorreto");
            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/aceitartermo")
    public ResponseEntity<String> aceitartermo(@RequestBody aceitartermo_dto dto) {

        try {
            String res = funcionario_service.aceitarTermo(dto.getEmail());
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





}
