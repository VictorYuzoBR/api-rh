package com.rh.api_rh.log;

import jakarta.persistence.*;
import lombok.Data;
import com.rh.api_rh.usuario.usuario_model;

import java.util.Date;
import java.util.UUID;


@Entity
@Data
public class log_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String registro;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private Date data;

    @Column(nullable = false)
    private String tipo;

}
