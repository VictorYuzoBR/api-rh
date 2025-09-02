package com.rh.api_rh.DTO.aplicacao.ferias;

import lombok.Data;

@Data
public class atualizarFerias_dto {

    private String novoStatus;
    private Long idferias;
    private String motivoRecusa;

}
