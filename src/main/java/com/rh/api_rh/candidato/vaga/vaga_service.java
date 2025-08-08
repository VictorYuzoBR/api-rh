package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.cadastrarVagaMapeado_dto;
import com.rh.api_rh.DTO.cadastrarVaga_dto;
import com.rh.api_rh.candidato.habilidade.habilidade_service;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_repository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Transactional
    public String cadastrar(cadastrarVaga_dto dto) {

        try {

            cadastrarVagaMapeado_dto dados = mapper.convert(dto);
            if (dados == null) {
                return null;
            } else {

                vaga_model vaga = vagarepository.save(dados.getVaga());
                habilidadeservice.cadastrarParaVaga(dados.getHabilidades(), vaga);

            }

        }  catch (Exception e) {
            return null;
        }
        return "sucesso";

    }

    public List<vaga_model> listar() {
        return vagarepository.findAll();
    }

    public List<vaga_habilidade_model> listarTabelaIntermediaria() {

        return vagahabilidaderepository.findAll();

    }


}
