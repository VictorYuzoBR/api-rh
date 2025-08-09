package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.cadastro.cadastrarVagaMapeado_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarVaga_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidatura_dto;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_repository;
import com.rh.api_rh.candidato.habilidade.habilidade_service;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_repository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class vaga_service {

    @Autowired
    private vaga_repository vagarepository;

    @Autowired
    private vaga_mapper mapper;

    @Autowired
    private habilidade_service habilidadeservice;

    @Autowired
    private vaga_habilidade_repository vagahabilidaderepository;

    @Autowired
    private candidato_repository candidatorepository;

    @Autowired
    private candidato_vaga_repository candidatovagarepository;

    @Transactional(rollbackOn =  Exception.class)
    public String cadastrar(cadastrarVaga_dto dto) {

        try {

            cadastrarVagaMapeado_dto dados = mapper.convert(dto);
            if (dados == null) {
                return null;
            } else {

                vaga_model vaga = vagarepository.save(dados.getVaga());
                String res = habilidadeservice.cadastrarParaVaga(dados.getHabilidades(), vaga);
                if (!res.equals("sucesso")) {
                    throw new IllegalStateException("uma das habilidade requisitadas não está cadastrada");
                }

            }

        }  catch (Exception e) {
            throw e;
        }
        return "sucesso";

    }

    public List<vaga_model> listar() {
        return vagarepository.findAll();
    }

    public List<vaga_habilidade_model> listarTabelaIntermediaria() {

        return vagahabilidaderepository.findAll();

    }

    public String candidatar(cadastroCandidatura_dto dto) {

        Optional<candidato_model> candidato = candidatorepository.findById(dto.getIdcandidato());
        Optional<vaga_model> vaga = vagarepository.findById(dto.getIdvaga());

        if(candidato.isPresent() && vaga.isPresent()) {
            candidato_vaga_model aplicacao = new candidato_vaga_model();
            aplicacao.setCandidato(candidato.get());
            aplicacao.setVaga(vaga.get());
            aplicacao.setFase("Triagem");
            candidatovagarepository.save(aplicacao);
            return "sucesso";
        } else {
            return "falha ao criar candidatura";
        }

    }


}
