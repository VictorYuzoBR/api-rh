package com.rh.api_rh.refreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface refresh_token_repository extends JpaRepository<refresh_token_model, Long> {
    Optional<refresh_token_model> findByIdusuario_Id(UUID id);

    Optional<refresh_token_model> findByRefreshtoken(String refresh_token);
}
