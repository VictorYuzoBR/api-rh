package com.rh.api_rh.candidato.candidato_idioma;

import com.rh.api_rh.candidato.idioma.idioma_model;
import com.rh.api_rh.candidato.candidato_model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ManyToAny;

@Data
@Entity
public class candidato_idioma_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idcandidato", referencedColumnName = "id")
    private candidato_model candidato;

    @ManyToOne
    @JoinColumn(name = "ididioma", referencedColumnName = "id")
    private idioma_model idioma;

    @Column(nullable = false)
    private String nivel;


}