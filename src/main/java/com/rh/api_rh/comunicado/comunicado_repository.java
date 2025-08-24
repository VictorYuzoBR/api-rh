package com.rh.api_rh.comunicado;

import com.rh.api_rh.funcionario.funcionario_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface comunicado_repository extends JpaRepository<comunicado_model, Long> {

}
