package com.rh.api_rh.candidato.experiencia;

import com.rh.api_rh.candidato.candidato_model;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class experiencia_service {

    @Autowired
    private experiencia_repository experiencia_repository;

    @Transactional(rollbackOn =  Exception.class)
    public String cadastrar(List<experiencia_model> experiencias, candidato_model candidato) {

        for  (experiencia_model experiencia : experiencias) {

            try {
                experiencia.setCandidato(candidato);
                experiencia_repository.save(experiencia);
            } catch (Exception e) {
                return ("Erro ao cadastrarParaCandidato experiencia");
            }

        }

        return ("sucesso");

    }

    public Boolean comparar(experiencia_model e1, experiencia_model e2) {

        if (e1 == null || e2 == null) return false;

        return Objects.equals(e1.getEmpresa(), e2.getEmpresa()) &&
                Objects.equals(e1.getDescricao(), e2.getDescricao()) &&
                Objects.equals(e1.getDataInicio(), e2.getDataInicio()) &&
                Objects.equals(e1.getDataFim(), e2.getDataFim());

    }

}
