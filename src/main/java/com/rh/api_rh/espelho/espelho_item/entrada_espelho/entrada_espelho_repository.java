package com.rh.api_rh.espelho.espelho_item.entrada_espelho;

import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface entrada_espelho_repository extends JpaRepository<entrada_espelho_model, Long> {

    List<entrada_espelho_model> findByItem(espelho_item_model item);

}
