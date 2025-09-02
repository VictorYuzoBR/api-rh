package com.rh.api_rh.DTO.cadastro;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class cadastrarFerias_dto {

    private UUID idfuncionario;
    private LocalDate dataInicio;
    private LocalDate dataFim;

}
