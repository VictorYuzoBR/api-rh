package com.rh.api_rh.candidato.vaga;

import com.rh.api_rh.DTO.cadastro.cadastrarVagaMapeado_dto;
import com.rh.api_rh.DTO.cadastro.cadastrarVaga_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidatura_dto;
import com.rh.api_rh.DTO.aplicacao.vaga.quantidadePessoasEtapa_dto;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_model;
import com.rh.api_rh.candidato.candidato_vaga.candidato_vaga_repository;
import com.rh.api_rh.candidato.candidato_vaga.etapas;
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

            Optional<candidato_vaga_model> jaexiste = candidatovagarepository.findByCandidatoAndVaga(candidato.get(), vaga.get());
            if(jaexiste.isPresent()) {
                return"candidato ja aplicou para esta vaga";
            }


            candidato_vaga_model aplicacao = new candidato_vaga_model();
            aplicacao.setCandidato(candidato.get());
            aplicacao.setVaga(vaga.get());
            aplicacao.setEtapa(etapas.TRIAGEM);
            candidatovagarepository.save(aplicacao);
            return "sucesso";
        } else {
            return "falha ao criar candidatura";
        }

    }

    public quantidadePessoasEtapa_dto calcularPessoasEtapa(Long idvaga) {
        quantidadePessoasEtapa_dto dto = new quantidadePessoasEtapa_dto();
        try {
            Optional<vaga_model> data =  vagarepository.findById(idvaga);

            if(data.isPresent()) {

                vaga_model vaga = data.get();
                List<candidato_vaga_model> aplicacoes = candidatovagarepository.findByVaga(vaga);

                Integer contagemTriagem = 0;
                Integer contagemEntrevistas = 0;
                Integer contagemOferta = 0;
                Integer contagemDesistencia = 0;
                Integer contagemFinalizado = 0;

                for (candidato_vaga_model aplicacao : aplicacoes) {
                    if (aplicacao.getEtapa().equals(etapas.TRIAGEM)) {
                        contagemTriagem++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.DESISTENCIA)) {
                        contagemDesistencia++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.FINALIZADO)) {
                        contagemFinalizado++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.OFERTA)) {
                        contagemOferta++;
                    }
                    if (aplicacao.getEtapa().equals(etapas.ENTREVISTA)) {
                        contagemEntrevistas++;
                    }
                }

                dto.getEtapas().put("TRIAGEM", contagemTriagem);
                dto.getEtapas().put("DESISTENCIA", contagemDesistencia);
                dto.getEtapas().put("FINALIZADO", contagemFinalizado);
                dto.getEtapas().put("OFERTA", contagemOferta);
                dto.getEtapas().put("ENTREVISTA", contagemEntrevistas);

            } else {
                return null;
            }



        } catch (Exception e) {
            return null;
        }
        return dto;


    }


}
