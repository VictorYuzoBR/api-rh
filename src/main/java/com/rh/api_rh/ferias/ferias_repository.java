package com.rh.api_rh.ferias;

import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.setor.setor_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ferias_repository extends JpaRepository<ferias_model, Long> {

    Optional<ferias_model> findByFuncionarioAndStatus(funcionario_model funcionario, String status);

    List<ferias_model> findByStatus(String status);

    List<ferias_model> findByFuncionario(funcionario_model funcionario);

    List<ferias_model> findBySetorfuncionarioAndStatus(String setor, String status);

}
