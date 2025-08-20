package com.rh.api_rh.infra.security.authTokens;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class candidato_token extends AbstractAuthenticationToken {
    private final String email;
    private final String password;

    public candidato_token(String email, String password) {
        super(null);
        this.email = email;
        this.password = password;
        setAuthenticated(false);
    }

    public candidato_token(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email;
        this.password = password;
        setAuthenticated(true);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}