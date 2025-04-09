package com.rh.api_rh.DTO;

import com.rh.api_rh.funcionario.Cargo;
import lombok.Data;


@Data
public class cadastro_dto {

    private String nome;

    private String data_nascimento;

    private String cpf;

    private String email;

    private Cargo cargo;

    private float salario;

    private String contabancaria;

    private String dataentrada;

    private String cep;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String estado;

    private String numero;

    private String complemento;

    private String numerotelefone;

    private String numerosetor;


}
