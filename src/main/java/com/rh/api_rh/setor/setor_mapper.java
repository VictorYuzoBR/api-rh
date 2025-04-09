package com.rh.api_rh.setor;

import com.rh.api_rh.DTO.setor_dto;
import org.springframework.stereotype.Service;

@Service
public class setor_mapper {

    public setor_model convert(setor_dto dto) {

        setor_model setor_model = new setor_model();
        setor_model.setNome(dto.getNome());
        return setor_model;

    }


}
