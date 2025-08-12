package com.rh.api_rh.candidato.candidato_habilidade;

import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.candidato_model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ManyToAny;

@Data
@Entity
public class candidato_habilidade_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idcandidato", referencedColumnName = "id")
    private candidato_model candidato;

    @ManyToOne
    @JoinColumn(name = "idhabilidade", referencedColumnName = "id")
    private habilidade_model habilidade;

    @Column(nullable = false)
    private Integer experienciaEmMeses;


}
