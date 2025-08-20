package com.rh.api_rh.candidato;

import com.rh.api_rh.DTO.aplicacao.candidato.enviarEmailNovaVaga_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidatoMapeado_dto;
import com.rh.api_rh.DTO.cadastro.cadastroCandidato_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_model;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_repository;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.experiencia.experiencia_service;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_service;
import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.habilidade.habilidade_model_apenas_formulario;
import com.rh.api_rh.candidato.habilidade.habilidade_service;
import com.rh.api_rh.candidato.idioma.idioma_model_apenas_formulario;
import com.rh.api_rh.candidato.idioma.idioma_service;
import com.rh.api_rh.util.email_service;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private email_service emailService;

    @Value("${SALT_SECRETWORD:!Senhasecreta1}")
    private String salt_secret;

    @Transactional(rollbackOn = Exception.class)
    public String cadastrar(cadastroCandidato_dto dto){

        cadastroCandidatoMapeado_dto dto_mapeado = mapper.convert(dto);

        List<experiencia_model> experiencias = dto_mapeado.getExperiencias();
        List<habilidade_model_apenas_formulario> habilidades = dto_mapeado.getHabilidades();
        List<idioma_model_apenas_formulario> idiomas = dto_mapeado.getIdiomas();
        List<formacaoAcademica_model> formacao =  dto_mapeado.getFormacaoAcademica();

        try {

            String palavraSalt = dto_mapeado.getCandidato().getEmail() + dto_mapeado.getCandidato().getPassword() + salt_secret;

            String senhahash = new BCryptPasswordEncoder().encode(palavraSalt);

            dto_mapeado.getCandidato().setPassword(senhahash);


            candidato_model candidato = candidatorepository.save(dto_mapeado.getCandidato());

            if (habilidades.size() > 0) {
                String res1 = habilidadeService.cadastrarParaCandidato(habilidades, candidato);
                if (!res1.equals("sucesso")) {
                    throw new IllegalArgumentException("Alguma habilidade não está no sistema");
                }
            }

            if (experiencias.size() > 0) {
                experienciaService.cadastrar(experiencias, candidato);
            }

            if (idiomas.size() > 0) {
                String res2 = idiomaService.cadastrarParaCandidato(idiomas, candidato);
                if  (!res2.equals("sucesso")) {
                    throw new IllegalArgumentException("Algum idioma não está no sistema");
                }
            }

            if (formacao.size() > 0) {
                formacaoAcademicaService.cadastrar(formacao, candidato);
            }


        } catch (Exception e) {
                throw e;
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

    public List<candidato_model> listarComBaseHabilidades(List<String> requisitos ){

        List<candidato_model> res =  new ArrayList<candidato_model>();

        List<candidato_model> todos = candidatorepository.findAll();

        Integer tamanhorequisitos = requisitos.size();

        for (candidato_model candidato : todos) {
            Integer cumprido = 0;
            List<candidato_habilidade_model> habilidadesCandidato = candidatohabilidaderepository.findByCandidato(candidato);

            for (candidato_habilidade_model habilidade : habilidadesCandidato) {
                String habilidadeString = habilidade.getHabilidade().getHabilidade();

                for (String requisito : requisitos) {

                    if (requisito.equals(habilidadeString)) {

                        cumprido++;
                        break;

                    }

                }

            }
            if (cumprido.equals(tamanhorequisitos)) {
                res.add(candidato);
            }

        }

        return res;

    }

    public String enviarEmailParaCandidatosComHabilidade(enviarEmailNovaVaga_dto dto) {

        if (dto.getCandidatos().isEmpty()) {
            return "lista vazia";
        }

        try {

            for (candidato_model candidato : dto.getCandidatos()) {

                String aux = emailService.enviarEmailNovaVaga(candidato, dto.getTituloVaga());
                if (!aux.equals("Email enviado com sucesso!")) {
                    return "erro ao enviar um dos emails";
                }

            }

            return "emails enviados com sucesso!";

        }  catch (Exception e) {
            return "erro ao enviar um dos emails";
        }

    }

    public candidato_model buscar(String email) {

        try{
            Optional<candidato_model> candidato = candidatorepository.findByEmail(email);
            if (candidato.isPresent()) {
                return candidato.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public String aceitartermo(Long id) {

        try {
            Optional<candidato_model> candidato = candidatorepository.findById(id);
            if (candidato.isPresent()) {
                candidato.get().setAceitouTermo(true);
                candidatorepository.save(candidato.get());
                return ("termo aceito");
            } else return null;
        } catch (Exception e) {
            return null;
        }
    }


}
