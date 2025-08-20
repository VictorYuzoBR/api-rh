package com.rh.api_rh.candidato.codigotrocasenhaCandidato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface codigotrocasenhaCandidato_repository extends JpaRepository<codigotrocasenhaCandidato_model, Long> {

    Optional<codigotrocasenhaCandidato_model> findByIdcandidato(Long id);


}
