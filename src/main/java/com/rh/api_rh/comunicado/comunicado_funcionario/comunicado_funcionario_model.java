package com.rh.api_rh.comunicado.comunicado_funcionario;

import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.comunicado.comunicado_model;
import com.rh.api_rh.funcionario.funcionario_model;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
public class comunicado_funcionario_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    @OnDelete(action = OnDeleteAction.CASCADE)
    private funcionario_model funcionario;

    @ManyToOne
    @JoinColumn
    private comunicado_model comunicado;

    @Column
    private Boolean visto = false;



}
