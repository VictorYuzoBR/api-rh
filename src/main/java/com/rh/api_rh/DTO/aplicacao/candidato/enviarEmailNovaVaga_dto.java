package com.rh.api_rh.DTO.aplicacao.candidato;

import com.rh.api_rh.candidato.candidato_model;
import lombok.Data;

import java.util.List;

@Data
public class enviarEmailNovaVaga_dto {

    private List<candidato_model> candidatos;
    private String tituloVaga;

}
