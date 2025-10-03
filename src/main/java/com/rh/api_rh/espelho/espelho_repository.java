package com.rh.api_rh.espelho;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface espelho_repository extends JpaRepository<espelho_model, Long> {

    Optional<espelho_model> findByRegistroAndPeriodoInicioAndPeriodoFim(String registro, LocalDate periodoInicio, LocalDate periodoFim);

    List<espelho_model> findByPeriodoInicio(LocalDate periodoInicio);

    Optional<espelho_model> findByRegistroAndPeriodoInicio(String registro, LocalDate periodoInicio);

    List<espelho_model> findByRegistro(String registro);

    Optional<espelho_model> findByPeriodoInicioAndRegistro(LocalDate periodoInicio, String registro);

}
