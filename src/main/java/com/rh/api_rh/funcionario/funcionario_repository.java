package com.rh.api_rh.funcionario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface funcionario_repository extends JpaRepository<funcionario_model, UUID> {

    Optional<funcionario_model> findByIdusuario_Registro(String registro);

    Optional<funcionario_model> findByEmail(String email);

    List<funcionario_model> findByCargo(Cargo cargo);

}
