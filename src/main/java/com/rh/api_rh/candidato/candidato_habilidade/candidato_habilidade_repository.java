package com.rh.api_rh.candidato.candidato_habilidade;

import com.rh.api_rh.candidato.candidato_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface candidato_habilidade_repository extends JpaRepository<candidato_habilidade_model, Long> {

    Optional<List<candidato_habilidade_model>> findByCandidatoId(Long id);

    List<candidato_habilidade_model> findByCandidato(candidato_model candidato);

    void deleteByCandidato(candidato_model candidato);

}
