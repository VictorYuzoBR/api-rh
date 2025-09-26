package com.rh.api_rh.setor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface setor_repository extends JpaRepository<setor_model, Long> {

    Optional<setor_model> findByNome(String nome);
}
