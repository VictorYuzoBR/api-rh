package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.aplicacao.vaga.*;
import com.rh.api_rh.DTO.cadastro.cadastrarVaga_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidatura_dto;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.etapas;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.util.email_service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor()
@RestController
@RequestMapping("/vaga")
public class vaga_controller {

    private final vaga_service vagaservice;

    private final vaga_application_service vagaapplicationservice;

    private final vaga_repository vagarepository;

    private final email_service emailService;

    private final token_service tokenservice;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody cadastrarVaga_dto dto) {

        try {

            String res = vagaservice.cadastrar(dto);
            if (res.equals("sucesso")) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }

        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<vaga_model>> listar() {

        try {
            List<vaga_model> res = vagaservice.listar();
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }   catch (Exception e) {
            return null;
        }

    }

    @GetMapping("/intermediaria")
    public  ResponseEntity<List<vaga_habilidade_model>> listarIntermediaria() {

        try {

            List<vaga_habilidade_model> res = vagaservice.listarTabelaIntermediaria();
            if (res != null) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            }

        } catch (Exception e) {
            return null;
        }

    }

    @PostMapping("/candidatura")
    public ResponseEntity<String> candidatar(@RequestBody cadastroCandidatura_dto dto) {

        try {
            String res = vagaservice.candidatar(dto);
            if (!res.equals("sucesso")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
            } else{
                return ResponseEntity.status(HttpStatus.OK).body(res);
            }
        }   catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/teste/{id}")
    public String calcularporcentagem(@PathVariable Long id) {

        Optional<vaga_model> test = vagarepository.findById(id);
        if (test.isPresent()) {
            vaga_model vaga = test.get();
            Integer res = vagaapplicationservice.calcularPorcentagemCandidatos(vaga);
            return("a porcentagem de candidados que possuem todas as habilidades é: " + res);
        }
        else return("erro");

    }

    @GetMapping("/etapas/{id}")
    public ResponseEntity<quantidadePessoasEtapa_dto> calcularPessoasEtapa(@PathVariable Long id) {

        try {

            quantidadePessoasEtapa_dto res =  vagaservice.calcularPessoasEtapa(id);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }    catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/melhores/{id}")
    public ResponseEntity<List<melhoresCandidatos_dto>> melhores(@PathVariable Long id) {
        try {

            Optional<vaga_model> vaga = vagarepository.findById(id);
            if (vaga.isPresent()) {
                List<melhoresCandidatos_dto> lista = vagaapplicationservice.melhoresCandidatos(vaga.get());
                return ResponseEntity.status(HttpStatus.OK).body(lista);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/listaCompatibilidades/{id}")
    public ResponseEntity<List<listaCompatibilidade_dto>> listaCompatibilidades(@PathVariable Long id) {

        try {

            List<listaCompatibilidade_dto> res = vagaapplicationservice.listarCompatibilidades(id);
            if (res != null) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/calcularCompatibilidade")
    public ResponseEntity<listaCompatibilidade_dto>  calcularCompatibilidade(@RequestBody compatibilidadeUnica_dto dto) {

        try {
            listaCompatibilidade_dto res = vagaapplicationservice.compatibilidadeUnica(dto);

            if (res != null) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/avancarEtapa")
    public ResponseEntity<candidato_vaga_model> avancarEtapa(@RequestBody avancarEtapa_dto dto) {

        try {
            candidato_vaga_model res = vagaapplicationservice.avancarEtapa(dto);
            if  (res != null) {
                return ResponseEntity.status(HttpStatus.OK).body(res);

            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/desistencia")
    public ResponseEntity<candidato_vaga_model> desistencia(@RequestBody avancarEtapa_dto dto) {

        try {
            candidato_vaga_model res = vagaapplicationservice.desistencia(dto);
            if  (res != null) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/finalizarAplicacao")
    public ResponseEntity<candidato_vaga_model> finalizar(@RequestBody avancarEtapa_dto dto) {

        try {
            candidato_vaga_model res = vagaapplicationservice.finalizarAplicacao(dto);
            if  (res != null) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PostMapping("/finalizarVaga")
    public ResponseEntity<String> finalizarVaga(@RequestBody finalizarVaga_dto dto, HttpServletRequest request) {

        try {

            UUID idrh = UUID.fromString(tokenservice.returnIdRh(request));

            String res = vagaservice.finalizarVaga(dto.getVagaid(), idrh);
            if (res.equals("sucesso")) {
                return ResponseEntity.status(HttpStatus.OK).body(res);
            } else {
                return ResponseEntity.badRequest().body(res);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/listarVagasAtivas")
    public ResponseEntity<List<vaga_model>> listarVagasAtivas() {

        try {
            List<vaga_model> res = vagaservice.listarVagasAtivas();
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }






}
