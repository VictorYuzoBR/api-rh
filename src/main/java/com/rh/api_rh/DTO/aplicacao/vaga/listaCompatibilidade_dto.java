package com.rh.api_rh.DTO.aplicacao.vaga;

import com.rh.api_rh.candidato.candidato_model;
import lombok.Data;

@Data
public class listaCompatibilidade_dto {

    private candidato_model candidato;
    private Integer compatibilidadeEmPorcentagem;

}
