package com.rh.api_rh.ferias;

import com.rh.api_rh.funcionario.funcionario_model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ferias_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private funcionario_model funcionario;

    @Column
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFim;

    @Column
    private String status;

    @Column
    private String setorfuncionario;

    @Column
    private String motivoRecusa;

    @ManyToOne
    @JoinColumn
    private funcionario_model atualizadoPor;


}
