package com.rh.api_rh.funcionario.fila_exclusao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface fila_exclusao_repository extends JpaRepository<fila_exclusao_model, Long> {

    List<fila_exclusao_model> findByDataexclusao(LocalDate dataexclusao);

}
