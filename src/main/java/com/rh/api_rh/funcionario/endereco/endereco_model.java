package com.rh.api_rh.funcionario.endereco;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class endereco_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 9)
    private String cep;

    @Column(nullable = false, length = 60)
    private String logradouro;

    @Column(nullable = false, length = 60)
    private String bairro;

    @Column(nullable = false, length = 60)
    private String cidade;

    @Column(nullable = false, length = 60)
    private String estado;

    @Column(nullable = false, length = 6)
    private String numero;

    @Column(nullable = true, length = 100)
    private String complemento;


}
