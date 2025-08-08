package com.rh.api_rh.funcionario.endereco;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface endereco_repository extends JpaRepository<endereco_model, Long> {
}
