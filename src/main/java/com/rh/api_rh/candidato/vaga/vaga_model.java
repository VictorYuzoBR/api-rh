package com.rh.api_rh.candidato.vaga;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class vaga_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String modelo;

    @Lob
    @Column
    private String descricao;

    @Column
    private String tipoContrato;

    @Column
    private String localizacao;

    @Column
    private String informacoes;

    @Column
    private String palavrasChave;

    @Column
    private String nivel;


}
