package com.rh.api_rh.espelho;

import com.rh.api_rh.espelho.espelho_item.espelho_item_model;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class espelho_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate periodoInicio;
    @Column
    private LocalDate periodoFim;

    /// dados empregador
    @Column
    private String empresa = "Bikube";
    @Column
    private String cnpj = "cnpj ficticio";
    @Column
    private String endereco = "Rua da Bikube 10";
    @Column
    private String bairro = "Bairro tal";
    @Column
    private String cidade = "Cidade tal";
    @Column
    private String estado = "SP";
    @Column
    private String cep = "00000-000";

    @Column
    private String nomeFuncionario;
    @Column
    private String funcao;
    @Column
    private String registro;
    @Column

    @JoinColumn
    @OneToMany
    private List<espelho_item_model> listaEntradas;

}
