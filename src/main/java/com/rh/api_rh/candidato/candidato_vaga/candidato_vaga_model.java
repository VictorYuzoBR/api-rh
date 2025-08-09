package com.rh.api_rh.candidato.candidato_vaga;

import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.vaga.vaga_model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class candidato_vaga_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idcandidato", referencedColumnName = "id")
    private candidato_model candidato;

    @ManyToOne
    @JoinColumn(name = "idvaga", referencedColumnName = "id")
    private vaga_model vaga;

    @Column
    private String fase;

}
