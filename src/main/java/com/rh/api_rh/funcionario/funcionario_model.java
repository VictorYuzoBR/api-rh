package com.rh.api_rh.funcionario;


import com.rh.api_rh.endereco.endereco_model;
import jakarta.persistence.*;
import com.rh.api_rh.setor.setor_model;
import com.rh.api_rh.telefone.telefone_model;
import com.rh.api_rh.usuario.usuario_model;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

    @OneToOne
    @JoinColumn(name = "idusuario", referencedColumnName = "id", nullable = false)
    private usuario_model idusuario;

    @OneToOne
    @JoinColumn(name = "id_telefone", referencedColumnName = "id", nullable = false)
    private telefone_model id_telefone;

    @ManyToOne
    @JoinColumn(name = "id_setor", referencedColumnName = "id",nullable = false)
    private setor_model id_setor;

    @ManyToOne
    @JoinColumn(name = "id_endereco", referencedColumnName = "id",nullable = false)
    private endereco_model id_endereco;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.cargo == Cargo.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_RH"), new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
        else if (this.cargo == Cargo.RH) return List.of(new SimpleGrantedAuthority("ROLE_RH"), new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
        else return List.of(new SimpleGrantedAuthority("ROLE_FUNCIONARIO"));
    }

    @Override
    public String getPassword() {
        return this.idusuario.getSenha();
    }

    @Override
    public String getUsername() {
        return this.idusuario.getRegistro();
    }
}
