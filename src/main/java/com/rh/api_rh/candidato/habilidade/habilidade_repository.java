package com.rh.api_rh.candidato.habilidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface habilidade_repository extends JpaRepository<habilidade_model, Long> {

    Optional<habilidade_model> findByHabilidade(String habilidade);
    
}
