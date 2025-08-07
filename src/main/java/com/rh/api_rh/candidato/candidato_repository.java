package com.rh.api_rh.candidato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface candidato_repository extends JpaRepository<candidato_model, Long> {

}
