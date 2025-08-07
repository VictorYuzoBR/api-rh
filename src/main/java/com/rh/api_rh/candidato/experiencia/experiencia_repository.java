package com.rh.api_rh.candidato.experiencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface experiencia_repository extends JpaRepository<experiencia_model, Long> {
}
