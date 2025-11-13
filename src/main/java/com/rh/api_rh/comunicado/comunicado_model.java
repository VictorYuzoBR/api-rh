package com.rh.api_rh.comunicado;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class comunicado_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Column
    private LocalDate datacriacao;
}
