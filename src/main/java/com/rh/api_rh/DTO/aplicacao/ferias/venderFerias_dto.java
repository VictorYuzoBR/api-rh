package com.rh.api_rh.DTO.aplicacao.ferias;

import lombok.Data;

import java.util.UUID;

@Data
public class venderFerias_dto {

    private UUID funcionarioid;
    private int quantidadeDias;

}
