package com.rh.api_rh.infra.security.providers;

import com.rh.api_rh.auth.funcionario_authorization_service;
import com.rh.api_rh.infra.security.authTokens.funcionario_token;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class funcionario_provider implements AuthenticationProvider {

    private final funcionario_authorization_service authorizationService;
    private final PasswordEncoder passwordEncoder;

    public funcionario_provider(funcionario_authorization_service authorizationService, PasswordEncoder passwordEncoder) {
        this.authorizationService = authorizationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof funcionario_token)) {
            return null;
        }

        funcionario_token token = (funcionario_token) authentication;
        String registro = token.getRegistro();
        String senha = (String) token.getCredentials();

        UserDetails userDetails = authorizationService.loadUserByUsername(registro);

        if (!passwordEncoder.matches(senha, userDetails.getPassword())) {
            throw new BadCredentialsException("Senha incorreta");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return funcionario_token.class.isAssignableFrom(authentication);
    }
}


