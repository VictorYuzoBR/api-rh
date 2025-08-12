package com.rh.api_rh.candidato.experiencia;

import com.rh.api_rh.candidato.candidato_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface experiencia_repository extends JpaRepository<experiencia_model, Long> {

    List<experiencia_model> findByCandidato(candidato_model candidato);

}
