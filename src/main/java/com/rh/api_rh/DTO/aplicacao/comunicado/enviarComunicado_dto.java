package com.rh.api_rh.DTO.aplicacao.comunicado;

import com.rh.api_rh.funcionario.funcionario_model;
import lombok.Data;

import java.util.List;

@Data
public class enviarComunicado_dto {

    private String titulo;

    private String texto;

    private List<funcionario_model> funcionarios;

}
