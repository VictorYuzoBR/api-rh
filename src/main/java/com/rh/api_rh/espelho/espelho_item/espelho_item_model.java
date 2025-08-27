package com.rh.api_rh.espelho.espelho_item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rh.api_rh.espelho.espelho_item.entrada_espelho.entrada_espelho_model;
import com.rh.api_rh.espelho.espelho_model;
import com.rh.api_rh.funcionario.funcionario_model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class espelho_item_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate data;

    @OneToMany(mappedBy = "item", orphanRemoval = true)
    private List<entrada_espelho_model> entradas;

    @Column
    private boolean ausencia;

    @Column
    private String descricaoAbono;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private espelho_model espelho;

}
