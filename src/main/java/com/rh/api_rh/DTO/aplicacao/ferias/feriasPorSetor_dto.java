package com.rh.api_rh.DTO.aplicacao.ferias;

import lombok.Data;

@Data
public class feriasPorSetor_dto {

    private String setor;
    private int quantidadeAndamento;
    private int quantidadeAprovadas;

}
