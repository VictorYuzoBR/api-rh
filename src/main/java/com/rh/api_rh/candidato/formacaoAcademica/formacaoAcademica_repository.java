package com.rh.api_rh.candidato.formacaoAcademica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface formacaoAcademica_repository extends JpaRepository<formacaoAcademica_model, Long> {
}
