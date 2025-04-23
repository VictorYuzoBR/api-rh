package com.rh.api_rh.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class usuario_model {

    @Id
    private UUID id;

    @Column(unique=true, nullable=false)
    private String registro;

    @Column(nullable=false)
    private String senha;

    @Column()
    private String status;

    @Column
    private int tentativas;

    @Column
    private Date databloqueio;

    @PrePersist
    public void prePersist(){
        if (this.status == null) {
            this.status = "ativo";
        }
    }

}
