package com.rh.api_rh.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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

    private List<String> habilidades;



}
