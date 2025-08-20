package com.rh.api_rh.candidato.codigotrocasenhaCandidato;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class codigotrocasenhaCandidato_model {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String codigo;

    @Column
    private Date data;

    @Column
    private Long idcandidato;



}
