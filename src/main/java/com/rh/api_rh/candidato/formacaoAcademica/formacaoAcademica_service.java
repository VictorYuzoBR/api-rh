package com.rh.api_rh.candidato.formacaoAcademica;

import com.rh.api_rh.candidato.candidato_model;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class formacaoAcademica_service {

    @Autowired
    private formacaoAcademica_repository formacaoAcademica_repository;

    @Transactional(rollbackOn = Exception.class)
    public String cadastrar(List<formacaoAcademica_model> formacaoAcademicas, candidato_model candidato) {

        for (formacaoAcademica_model formacaoAcademica : formacaoAcademicas) {

            try {
                formacaoAcademica.setCandidato(candidato);
                formacaoAcademica_repository.save(formacaoAcademica);
            } catch (Exception e) {
                return ("erro ao salvar formacaoAcademica");
            }

        }

        return ("sucesso");
    }

}
