package com.rh.api_rh.telefone;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class telefone_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String numero;



}
