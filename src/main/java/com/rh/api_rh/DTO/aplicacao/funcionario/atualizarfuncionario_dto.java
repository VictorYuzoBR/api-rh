package com.rh.api_rh.DTO.aplicacao.funcionario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class atualizarfuncionario_dto {

    /// enviar o antigo para conseguir procurar no repository
    @NotBlank
    private String email;

    @NotBlank
    private String funcao;

    @NotNull
    private Float salario;

    @NotBlank
    private String contabancaria;

    @NotNull
    private Long idsetor;

    /// enviar o antigo para conseguir procurar no repository
    @NotBlank
    private String telefone;

    private String telefonenovo;

    private String emailnovo;

}
