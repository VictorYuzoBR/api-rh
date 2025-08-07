package com.rh.api_rh.candidato.habilidade;

import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_model;
import com.rh.api_rh.candidato.candidato_habilidade.candidato_habilidade_repository;
import com.rh.api_rh.candidato.candidato_model;
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

    @Transactional(rollbackOn =  Exception.class)
    public String cadastrar(List<habilidade_model_apenas_formulario> habilidades, candidato_model candidato) {

        for (habilidade_model_apenas_formulario habilidade : habilidades) {
            try {

                candidato_habilidade_model auxiliar = new candidato_habilidade_model();
                Optional<habilidade_model> jaexistente = habilidadeRepository.findByHabilidade(habilidade.getHabilidade());

                if (jaexistente.isEmpty()) {

                    habilidade_model habilidadeParaCadastrar = new habilidade_model();
                    habilidadeParaCadastrar.setHabilidade(habilidade.getHabilidade());

                    habilidadeRepository.save(habilidadeParaCadastrar);


                    auxiliar.setCandidato(candidato);
                    auxiliar.setHabilidade(habilidadeParaCadastrar);
                    auxiliar.setExperienciaEmAnos(habilidade.getTempoExperiencia());

                    candidatoHabilidadeRepository.save(auxiliar);

                } else {
                    auxiliar.setCandidato(candidato);
                    auxiliar.setHabilidade(jaexistente.get());
                    auxiliar.setExperienciaEmAnos(habilidade.getTempoExperiencia());
                    candidatoHabilidadeRepository.save(auxiliar);
                }
            } catch (Exception e) {
                return ("falha ao cadastrar alguma das habilidades");
            }
        }

        return "sucesso";

    }

}
