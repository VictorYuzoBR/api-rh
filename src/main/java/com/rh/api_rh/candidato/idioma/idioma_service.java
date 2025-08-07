package com.rh.api_rh.candidato.idioma;

import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_model;
import com.rh.api_rh.candidato.candidato_idioma.candidato_idioma_repository;
import com.rh.api_rh.candidato.candidato_model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class idioma_service {

    @Autowired
    private idioma_repository idiomarepository;

    @Autowired
    private candidato_idioma_repository candidatoidiomarepository;

    public String cadastrar(List<idioma_model_apenas_formulario> idiomas, candidato_model candidato) {

        for (idioma_model_apenas_formulario idioma : idiomas) {

            try {
                candidato_idioma_model auxiliar =  new candidato_idioma_model();
                Optional<idioma_model> jaexistente = idiomarepository.findByIdioma(idioma.getIdioma());

                if (jaexistente.isPresent()) {

                    auxiliar.setIdioma(jaexistente.get());
                    auxiliar.setCandidato(candidato);
                    auxiliar.setNivel(idioma.getNivel());
                    candidatoidiomarepository.save(auxiliar);

                } else {

                    idioma_model idiomaParaCadastrar = new idioma_model();
                    idiomaParaCadastrar.setIdioma(idioma.getIdioma());
                    idiomarepository.save(idiomaParaCadastrar);

                    auxiliar.setIdioma(idiomaParaCadastrar);
                    auxiliar.setCandidato(candidato);
                    auxiliar.setNivel(idioma.getNivel());
                    candidatoidiomarepository.save(auxiliar);

                }




            } catch (Exception e) {
                return("falha ao cadastrar alguma das habilidades");
            }

        }
        return("sucesso");

    }

}
