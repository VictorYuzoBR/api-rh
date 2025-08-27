package com.rh.api_rh.espelho.espelho_item.entrada_espelho;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import com.rh.api_rh.espelho.espelho_model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
public class entrada_espelho_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tipo;

    @Column
    private LocalTime hora;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private espelho_item_model item;


}
