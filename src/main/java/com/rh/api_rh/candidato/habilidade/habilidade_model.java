package com.rh.api_rh.candidato.habilidade;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class habilidade_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String habilidade;


}
