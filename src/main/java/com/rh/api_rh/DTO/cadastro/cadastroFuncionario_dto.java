package com.rh.api_rh.DTO.cadastro;

import com.rh.api_rh.funcionario.Cargo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class cadastroFuncionario_dto {

    @NotBlank
    private String nome;

    @NotBlank
    private String funcao;

    @NotBlank
    private String data_nascimento;

    @NotBlank
    private String cpf;

    @NotBlank
    private String email;


    private Cargo cargo;

    @NotNull
    private Float salario;

    @NotBlank
    private String contabancaria;

    @NotBlank
    private String dataentrada;

    @NotBlank
    private String cep;

    @NotBlank
    private String logradouro;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;

    @NotBlank
    private String numero;

    private String complemento;

    @NotBlank
    private String numerotelefone;

    @NotNull
    private String numerosetor;


}
