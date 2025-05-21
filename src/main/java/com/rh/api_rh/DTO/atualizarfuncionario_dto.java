package com.rh.api_rh.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class atualizarfuncionario_dto {

    private String emailfuncionariorh;
    private String email;
    private String funcao;
    private float salario;
    private String contabancaria;
    private Long idsetor;
    private String telefone;

}
