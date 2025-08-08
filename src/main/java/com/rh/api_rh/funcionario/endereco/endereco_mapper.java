package com.rh.api_rh.funcionario.endereco;

import com.rh.api_rh.DTO.cadastro_dto;
import org.springframework.stereotype.Service;

@Service
public class endereco_mapper {

    public endereco_model convert(cadastro_dto dto) {

        endereco_model endereco = new endereco_model();

        endereco.setNumero(dto.getNumero());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setBairro(dto.getBairro());
        endereco.setCep(dto.getCep());
        endereco.setComplemento(dto.getComplemento());
        endereco.setLogradouro(dto.getLogradouro());
        return endereco;

    }

}
