package com.rh.api_rh.candidato.formacaoAcademica;

import com.rh.api_rh.candidato.candidato_model;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    public boolean comparar (formacaoAcademica_model f1, formacaoAcademica_model f2) {

        if (f1 == null || f2 == null) return false;

        return Objects.equals(f1.getInstituicao(), f2.getInstituicao()) &&
                Objects.equals(f1.getCurso(), f2.getCurso()) &&
                Objects.equals(f1.getSituacao(), f2.getSituacao()) &&
                Objects.equals(f1.getDataInicio(), f2.getDataInicio()) &&
                Objects.equals(f1.getDataFim(), f2.getDataFim());

    }

    public List<formacaoAcademica_model> listar() {
        return formacaoAcademica_repository.findAll();
    }

}
