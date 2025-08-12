package com.rh.api_rh.DTO.response;

import com.rh.api_rh.candidato.candidato_model;
import lombok.Data;

@Data
public class melhoresCandidatos_dto {

    private candidato_model candidato;
    private Integer pontuacao;

}
