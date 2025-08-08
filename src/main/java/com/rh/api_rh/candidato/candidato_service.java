package com.rh.api_rh.candidato;

import com.rh.api_rh.DTO.cadastroCandidatoMapeado_dto;
import com.rh.api_rh.DTO.cadastroCandidato_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_model;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_repository;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.experiencia.experiencia_service;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_service;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.habilidade.habilidade_service;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class candidato_service {

    @Autowired
    private candidato_repository candidatorepository;

    @Autowired
    private habilidade_service habilidadeService;

    @Autowired
    private experiencia_service experienciaService;

    @Autowired
    private formacaoAcademica_service formacaoAcademicaService;

    @Autowired
    private idioma_service idiomaService;

    @Autowired
    private candidato_habilidade_repository candidatohabilidaderepository;

    @Autowired
    private candidato_idioma_repository candidatoidiomarepository;


    @Autowired
    private cadastroCandidatoMapper mapper;

    @Transactional
    public String cadastrar(cadastroCandidato_dto dto){

        cadastroCandidatoMapeado_dto dto_mapeado = mapper.convert(dto);

        List<experiencia_model> experiencias = dto_mapeado.getExperiencias();
        List<habilidade_model_apenas_formulario> habilidades = dto_mapeado.getHabilidades();
        List<idioma_model_apenas_formulario> idiomas = dto_mapeado.getIdiomas();
        List<formacaoAcademica_model> formacao =  dto_mapeado.getFormacaoAcademica();

        try {

            candidato_model candidato = candidatorepository.save(dto_mapeado.getCandidato());

            habilidadeService.cadastrarParaCandidato(habilidades, candidato);
            experienciaService.cadastrar(experiencias, candidato);
            idiomaService.cadastrar(idiomas, candidato);
            formacaoAcademicaService.cadastrar(formacao, candidato);


        } catch (Exception e) {
            return("falha ao cadastrarParaCandidato");
        }

        return("candidato criado com sucesso ");

    }

    public List<candidato_model> listar(){
        try {
            return candidatorepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public List<candidato_habilidade_model> listarhabilidades(Long id){
        try {
            Optional<List<candidato_habilidade_model>> lista = candidatohabilidaderepository.findByCandidatoId(id);

            if (lista.isPresent()) {
                return lista.get();
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<candidato_idioma_model> listaridiomas(Long id){
        try {
            Optional<List<candidato_idioma_model>> lista = candidatoidiomarepository.findByCandidatoId(id);

            if (lista.isPresent()) {
                return lista.get();
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }



}
