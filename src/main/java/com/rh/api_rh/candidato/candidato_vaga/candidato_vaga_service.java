package com.rh.api_rh.candidato.candidato_vaga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class candidato_vaga_service {

    @Autowired
    private candidato_vaga_repository candidatovagaRepository;

    public List<candidato_vaga_model> listar() {
        return candidatovagaRepository.findAll();
    }


}
