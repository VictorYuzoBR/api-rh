package com.rh.api_rh.setor;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class setor_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30, unique = true)
    private String nome;


}
