package com.rh.api_rh.comunicado.comunicado_funcionario;

import com.rh.api_rh.funcionario.funcionario_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface comunicado_funcionario_repository extends JpaRepository<comunicado_funcionario_model, Long> {

    List<comunicado_funcionario_model> findByFuncionario(funcionario_model funcionario);

}
