package com.rh.api_rh.log;

import jakarta.persistence.*;
import lombok.Data;
import com.rh.api_rh.usuario.usuario_model;

import java.util.Date;


@Entity
@Data
public class log_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id",nullable = false)
    private usuario_model usuario_id;

    @Column(nullable = false)
    private String acao;

    @Column(nullable = false)
    private Date data;

}
