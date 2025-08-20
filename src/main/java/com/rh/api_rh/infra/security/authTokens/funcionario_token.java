package com.rh.api_rh.infra.security.authTokens;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class funcionario_token extends AbstractAuthenticationToken {
    private final String registro;
    private final String senha;

    public funcionario_token(String registro, String senha) {
        super(null);
        this.registro = registro;
        this.senha = senha;
        setAuthenticated(false);
    }

    public funcionario_token(String registro, String senha, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.registro = registro;
        this.senha = senha;
        setAuthenticated(true);
    }

    public String getRegistro() {
        return registro;
    }

    @Override
    public Object getCredentials() {
        return senha;
    }

    @Override
    public Object getPrincipal() {
        return registro;
    }
}