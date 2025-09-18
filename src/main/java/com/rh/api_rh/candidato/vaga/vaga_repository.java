package com.rh.api_rh.candidato.vaga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface vaga_repository extends JpaRepository<vaga_model, Long> {

    List<vaga_model> findByStatus(String status);

}
