package com.rh.api_rh.candidato.idioma;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface idioma_repository extends JpaRepository<idioma_model, Long> {

    Optional<idioma_model> findByIdioma(String idioma);

}
