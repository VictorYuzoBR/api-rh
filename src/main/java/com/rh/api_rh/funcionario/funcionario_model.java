package com.rh.api_rh.funcionario;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rh.api_rh.funcionario.endereco.endereco_model;
import jakarta.persistence.*;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.funcionario.telefone.telefone_model;
import com.rh.api_rh.usuario.usuario_model;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class funcionario_model implements UserDetails {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;


    @Column(nullable = false)
    private String data_nascimento;

    @Column(unique=true, nullable = false)
    private String cpf;

    @Column(unique=true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Cargo cargo;

    @Column(nullable = false)
    private float salario;

    @Column(nullable = false)
    private String contabancaria;

    @Column(nullable = false)
    private String dataentrada;

    @Column(nullable = true)
    private String datasaida;

    @Column(nullable = false)
    private String funcao;

    @Column
    private String status = "ativo";

    @Column
    private int feriasDisponiveis = 0;

    @Column
    private int fracoesDisponiveis = 0;

    @Column
    private LocalDate ultimoCalculo;

    @Column
    private boolean periodo14dias = false;

    @Column
    private boolean deFerias = false;

    @Column
    private int saldoAtestado = 0;

    @Column
    private int venderFerias = 0;


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "idusuario", referencedColumnName = "id", nullable = false)
    private usuario_model idusuario;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_telefone", referencedColumnName = "id", nullable = false)
    private telefone_model id_telefone;

    @ManyToOne
    @JoinColumn(name = "id_setor", referencedColumnName = "id",nullable = false)
    private setor_model idsetor;

    @ManyToOne
    @JoinColumn(name = "id_endereco", referencedColumnName = "id",nullable = false)
    private endereco_model id_endereco;


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.cargo == Cargo.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_RH"), new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
        else if (this.cargo == Cargo.RH) return List.of(new SimpleGrantedAuthority("ROLE_RH"), new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
        else return List.of(new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return this.idusuario.getSenha();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return this.idusuario.getRegistro();
    }
}
