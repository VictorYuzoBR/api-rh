package com.rh.api_rh.candidato.candidato_vaga;

import com.rh.api_rh.candidato.vaga.vaga_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface candidato_vaga_repository extends JpaRepository<candidato_vaga_model, Long> {

    List<candidato_vaga_model> findByVaga(vaga_model vaga);

}
