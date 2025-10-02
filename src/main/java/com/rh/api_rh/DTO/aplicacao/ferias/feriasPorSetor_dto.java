package com.rh.api_rh.DTO.aplicacao.ferias;

import com.rh.api_rh.ferias.ferias_model;
import lombok.Data;

import java.util.List;

@Data
public class feriasPorSetor_dto {

    private String setor;
    private int quantidadeAndamento;
    private int quantidadeAprovadas;
    private List<ferias_model> solicitacoes;

}
