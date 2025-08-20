package com.rh.api_rh.candidato;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rh.api_rh.candidato.experiencia.experiencia_model;
import com.rh.api_rh.candidato.formacaoAcademica.formacaoAcademica_model;
import com.rh.api_rh.funcionario.Cargo;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class candidato_model implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
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

    @Column
    private Boolean aceitouTermo = false;

    @OneToMany(mappedBy = "candidato", orphanRemoval = true)
    private List<formacaoAcademica_model> formacoes;

    @OneToMany(mappedBy = "candidato", orphanRemoval = true)
    private List<experiencia_model> experiencias;

    @Column()
    private String status = "ativo";

    @Column
    private int tentativas = 0;

    @Column
    private Date databloqueio;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }



}
