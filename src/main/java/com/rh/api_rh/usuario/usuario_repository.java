package com.rh.api_rh.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface usuario_repository extends JpaRepository<usuario_model, UUID> {

    Optional<usuario_model> findByRegistro (String registro);

}


