package com.rh.api_rh.funcionario.fila_exclusao;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class fila_exclusao_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID idfuncionario;

    @Column
    private LocalDate dataexclusao;

}
