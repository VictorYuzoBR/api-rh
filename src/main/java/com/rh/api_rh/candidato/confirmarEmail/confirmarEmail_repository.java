package com.rh.api_rh.candidato.confirmarEmail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface confirmarEmail_repository extends JpaRepository<confirmarEmail_model, Long> {

    Optional<confirmarEmail_model>  findByEmail(String email);

}
