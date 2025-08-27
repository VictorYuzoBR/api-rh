package com.rh.api_rh.espelho.espelho_item;

import com.rh.api_rh.espelho.espelho_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface espelho_item_repository extends JpaRepository<espelho_item_model, Long> {

    Optional<espelho_item_model> findByDataAndEspelho(LocalDate data, espelho_model espelho);

    List<espelho_item_model> findByData(LocalDate data);

}
