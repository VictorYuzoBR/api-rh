package com.rh.api_rh.telefone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface telefone_repository extends JpaRepository<telefone_model, Long> {

    Optional<telefone_model> findByNumero(String numero);

}
