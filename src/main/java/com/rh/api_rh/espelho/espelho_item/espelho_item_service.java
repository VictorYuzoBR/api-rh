package com.rh.api_rh.espelho.espelho_item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class espelho_item_service {

    @Autowired
    private espelho_item_repository espelhoItemRepository;

    public espelho_item_model buscarItemPorId(Long id) {

        Optional<espelho_item_model> optional = espelhoItemRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }   else {
            return null;
        }

    }

}
