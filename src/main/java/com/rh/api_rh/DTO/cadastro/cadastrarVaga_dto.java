package com.rh.api_rh.DTO.cadastro;

import com.rh.api_rh.candidato.habilidade.habilidade_apenas_formulario_vaga;
import lombok.Data;

import java.util.List;

@Data
public class cadastrarVaga_dto {


    private String titulo;


    private String modelo;


    private String descricao;


    private String tipoContrato;


    private String localizacao;


    private String informacoes;


    private String palavrasChave;

    private List<habilidade_apenas_formulario_vaga> habilidades;



}
