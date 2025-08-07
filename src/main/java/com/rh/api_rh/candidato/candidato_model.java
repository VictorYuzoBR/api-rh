package com.rh.api_rh.candidato;

import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.candidato.habilidade.habilidade_model;
import com.rh.api_rh.candidato.idioma.idioma_model;
import com.rh.api_rh.telefone.telefone_model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class candidato_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String password;

    @Column
    private String nome;

    @Column(unique=true)
    private String email;

    @Column
    private String estado;

    @Column String cidade;

    /// como este telefone será apenas para contato, não será do tipo telefone_model, pois a pessoa ainda não faz parte da empresa e não precisa de rastreabilidade
    @Column
    private String telefone;

    @Column
    private String linkedin;

    @Column
    private String github;

    @OneToMany(mappedBy = "candidato", orphanRemoval = true)
    private List<formacaoAcademica_model> formacoes;

    @OneToMany(mappedBy = "candidato", orphanRemoval = true)
    private List<experiencia_model> experiencias;




}
