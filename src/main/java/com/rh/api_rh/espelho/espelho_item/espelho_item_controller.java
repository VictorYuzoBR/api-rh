package com.rh.api_rh.espelho.espelho_item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/espelhoItem")
@RequiredArgsConstructor
public class espelho_item_controller {

    private final espelho_item_service espelho_item_service;

    @GetMapping("/{id}")
    public ResponseEntity<espelho_item_model> buscarEntradaPorId(@PathVariable Long id) {

        try {

            espelho_item_model res = espelho_item_service.buscarItemPorId(id);
            if (res != null) {
                return ResponseEntity.ok(res);
            } else
                return ResponseEntity.notFound().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
