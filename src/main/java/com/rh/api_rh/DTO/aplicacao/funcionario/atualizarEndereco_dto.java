package com.rh.api_rh.DTO.aplicacao.funcionario;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.UUID;

@Data
public class atualizarEndereco_dto {

    private UUID funcionarioid;

    private String cep;

    private String logradouro;

    private String bairro;

    private String cidade;

    private String estado;

    private String numero;

    private String complemento;

}
