package com.rh.api_rh.candidato.vaga_habilidade;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.InterfaceAddress;

@Repository
public interface vaga_habilidade_repository extends JpaRepository<vaga_habilidade_model, Long> {
}
