package com.rh.api_rh.infra.security.providers;

import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.infra.security.authTokens.candidato_token;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



import com.rh.api_rh.infra.security.authTokens.candidato_token;
import com.rh.api_rh.auth.candidato_authorization_service;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class candidato_provider implements AuthenticationProvider {

    private final candidato_authorization_service candidatoDetailsService;
    private final PasswordEncoder passwordEncoder;

    public candidato_provider(candidato_authorization_service candidatoDetailsService, PasswordEncoder passwordEncoder) {
        this.candidatoDetailsService = candidatoDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof candidato_token)) {
            return null;
        }

        candidato_token token = (candidato_token) authentication;
        String email = token.getEmail();
        String rawPassword = (String) token.getCredentials();

        UserDetails candidato = candidatoDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(rawPassword, candidato.getPassword())) {
            throw new BadCredentialsException("Senha incorreta");
        }

        // Cria token autenticado com os authorities carregados do UserDetails
        return new UsernamePasswordAuthenticationToken(
                candidato,
                null,
                candidato.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return candidato_token.class.isAssignableFrom(authentication);
    }
}

