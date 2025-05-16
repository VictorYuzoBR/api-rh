package com.rh.api_rh.refreshToken;

import com.rh.api_rh.usuario.usuario_model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class refresh_token_model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshtoken;

    @OneToOne
    @JoinColumn(name = "idusuario", referencedColumnName = "id", nullable = false)
    private usuario_model idusuario;


}
