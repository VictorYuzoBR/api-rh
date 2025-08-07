package com.rh.api_rh.candidato.experiencia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rh.api_rh.candidato.candidato_model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class experiencia_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String empresa;

    @Column
    private String descricao;

    @Column
    private Date dataInicio;

    @Column
    private Date dataFim;

    @ManyToOne
    @JoinColumn(name = "candidato_id")
    @JsonIgnore
    private candidato_model candidato;

}
