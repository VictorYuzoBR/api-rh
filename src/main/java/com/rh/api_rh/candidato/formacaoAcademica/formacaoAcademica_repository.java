package com.rh.api_rh.candidato.formacaoAcademica;

import com.rh.api_rh.candidato.candidato_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface formacaoAcademica_repository extends JpaRepository<formacaoAcademica_model, Long> {

    List<formacaoAcademica_model> findByCandidato(candidato_model candidato);

}
