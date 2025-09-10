package com.rh.api_rh.DTO.aplicacao.funcionario;

import lombok.Data;

@Data
public class atualizarfuncionario_dto {
    
    private String email;
    private String funcao;
    private float salario;
    private String contabancaria;
    private Long idsetor;
    private String telefone;

}
