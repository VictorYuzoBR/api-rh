package com.rh.api_rh.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.rh.api_rh.funcionario.funcionario_model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class token_service {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(funcionario_model funcionario) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Yuzo")
                    .withSubject(funcionario.getId().toString())
                    .withExpiresAt(generateExpiration())
                    .sign(algorithm);
            return token;
        } catch (
                JWTCreationException e
        ) {
            throw new RuntimeException("Erro ao gerar token", e);
        }

    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Yuzo")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }



    public Instant generateExpiration() {
        return LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.of("-03:00"));
    }

}
