package com.rh.api_rh.candidato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface candidato_repository extends JpaRepository<candidato_model, Long> {

    Optional<candidato_model> findByEmail(String email);

}
