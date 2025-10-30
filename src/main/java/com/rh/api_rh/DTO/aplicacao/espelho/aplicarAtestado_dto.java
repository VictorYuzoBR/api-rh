package com.rh.api_rh.DTO.aplicacao.espelho;

import lombok.Data;

import java.util.UUID;

@Data
public class aplicarAtestado_dto {

    private UUID funcionarioid;
    private int diasDeAtestado;

}
