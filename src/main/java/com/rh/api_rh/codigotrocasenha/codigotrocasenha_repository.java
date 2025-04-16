package com.rh.api_rh.codigotrocasenha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface codigotrocasenha_repository extends JpaRepository<codigotrocasenha_model, Long> {

    codigotrocasenha_model findByIdusuario(UUID idusuario);

    void deleteByIdusuario(UUID idusuario);

}
