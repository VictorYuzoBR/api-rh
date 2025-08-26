package com.rh.api_rh.candidato.confirmarEmail;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class confirmarEmail_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    String email;

    @Column
    String codigo;

    @Column
    private Date data;

}
