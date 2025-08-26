package com.rh.api_rh.espelho.espelho_item;

import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_model;
import com.rh.api_rh.funcionario.funcionario_model;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class espelho_item_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate data;

    @Column
    private boolean ausencia;

    @Column
    private boolean descricaoAbono;

    @ManyToOne
    @JoinColumn
    private funcionario_model funcionario;

}
