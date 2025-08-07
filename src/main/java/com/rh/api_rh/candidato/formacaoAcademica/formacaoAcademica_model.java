package com.rh.api_rh.candidato.formacaoAcademica;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rh.api_rh.candidato.candidato_model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class formacaoAcademica_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String instituicao;

    @Column
    private String curso;

    @Column
    private String situacao;

    @Column
    private Date dataInicio;

    @Column
    private Date dataFim;

    @ManyToOne
    @JoinColumn(name = "candidato_id")
    @JsonIgnore
    private candidato_model candidato;


}
