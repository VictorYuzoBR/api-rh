package com.rh.api_rh.candidato.idioma;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class idioma_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column()
    private String idioma;

}
