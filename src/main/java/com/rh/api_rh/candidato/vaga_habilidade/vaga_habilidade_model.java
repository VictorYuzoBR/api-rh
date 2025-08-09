package com.rh.api_rh.candidato.vaga_habilidade;

import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.vaga.vaga_model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class vaga_habilidade_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private vaga_model vaga;

    @ManyToOne
    private habilidade_model habilidade;

    @Column(nullable = false)
    private Integer peso;


}
