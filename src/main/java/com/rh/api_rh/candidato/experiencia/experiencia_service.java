package com.rh.api_rh.candidato.experiencia;

import com.rh.api_rh.candidato.candidato_model;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                return ("Erro ao cadastrar experiencia");
            }

        }

        return ("sucesso");

    }

}
