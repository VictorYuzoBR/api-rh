package com.rh.api_rh.funcionario.telefone;

import com.rh.api_rh.DTO.cadastro_dto;
import org.springframework.stereotype.Service;

@Service
public class telefone_mapper {

    public telefone_model convert(cadastro_dto dto) {

        telefone_model telefone = new telefone_model();
        telefone.setNumero(dto.getNumerotelefone());
        return telefone;

    }

}
