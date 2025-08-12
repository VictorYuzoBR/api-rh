package com.rh.api_rh.candidato.habilidade;

import com.rh.api_rh.DTO.cadastro.cadastroHabilidade_dto;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.vaga.vaga_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_model;
import com.rh.api_rh.candidato.vaga_habilidade.vaga_habilidade_repository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class habilidade_service {

    @Autowired
    private habilidade_repository habilidadeRepository;

    @Autowired
    private candidato_habilidade_repository candidatoHabilidadeRepository;

    @Autowired
    private vaga_habilidade_repository  vagaHabilidadeRepository;

    @Transactional(rollbackOn =  Exception.class)
    public String cadastrarParaCandidato(List<habilidade_model_apenas_formulario> habilidades, candidato_model candidato) {

        for (habilidade_model_apenas_formulario habilidade : habilidades) {
            try {

                candidato_habilidade_model auxiliar = new candidato_habilidade_model();
                Optional<habilidade_model> jaexistente = habilidadeRepository.findByHabilidade(habilidade.getHabilidade());

                if (jaexistente.isEmpty()) {

                    throw new IllegalArgumentException("Habilidade não existe no banco de dados");

                } else {
                    auxiliar.setCandidato(candidato);
                    auxiliar.setHabilidade(jaexistente.get());
                    auxiliar.setExperienciaEmMeses(habilidade.getTempoExperiencia());
                    candidatoHabilidadeRepository.save(auxiliar);
                }
            } catch (Exception e) {
                throw e;
            }
        }

        return "sucesso";

    }


    @Transactional(rollbackOn = Exception.class)
    public String cadastrarParaVaga(List<habilidade_apenas_formulario_vaga> habilidades, vaga_model vaga) {

        for (habilidade_apenas_formulario_vaga habilidade : habilidades) {

            try {

                vaga_habilidade_model auxiliar = new vaga_habilidade_model();
                Optional<habilidade_model> jaexistente = habilidadeRepository.findByHabilidade(habilidade.getHabilidade());

                if (jaexistente.isEmpty()) {

                    throw new IllegalArgumentException("habilidade não existe no banco de dados");


                } else {
                    auxiliar.setVaga(vaga);
                    auxiliar.setHabilidade(jaexistente.get());
                    auxiliar.setPeso(habilidade.getPeso());

                    vagaHabilidadeRepository.save(auxiliar);
                }

            } catch (Exception e) {
                throw e;
            }


        }

        return "sucesso";


    }

    public List<habilidade_model> listar() {
        return habilidadeRepository.findAll();
    }

    public habilidade_model cadastrarHabilidade(cadastroHabilidade_dto dto) {
        habilidade_model habilidade = new habilidade_model();
        habilidade.setHabilidade(dto.getHabilidade());
        try {
            habilidadeRepository.save(habilidade);
        }  catch (Exception e) {
            return null;
        }
        return habilidade;

    }


}
