package com.rh.api_rh.DTO;

import com.rh.api_rh.candidato.habilidade.habilidade_apenas_formulario_vaga;
import com.rh.api_rh.candidato.vaga.vaga_model;
import lombok.Data;

import java.util.List;

@Data
public class cadastrarVagaMapeado_dto {

    private vaga_model vaga;

    private List<habilidade_apenas_formulario_vaga> habilidades;

}
