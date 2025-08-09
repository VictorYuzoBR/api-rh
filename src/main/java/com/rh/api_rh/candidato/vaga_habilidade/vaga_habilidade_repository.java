package com.rh.api_rh.candidato.vaga_habilidade;


import com.rh.api_rh.candidato.vaga.vaga_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.net.InterfaceAddress;
import java.util.List;

@Repository
public interface vaga_habilidade_repository extends JpaRepository<vaga_habilidade_model, Long> {

    List<vaga_habilidade_model> findByVaga(vaga_model vaga);
}
