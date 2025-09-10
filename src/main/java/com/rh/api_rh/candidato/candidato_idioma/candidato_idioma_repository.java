package com.rh.api_rh.candidato.candidato_idioma;

import com.rh.api_rh.candidato.candidato_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface candidato_idioma_repository extends JpaRepository<candidato_idioma_model, Long> {

    Optional<List<candidato_idioma_model>> findByCandidatoId(Long id);

    void deleteByCandidato (candidato_model candidato);

}
