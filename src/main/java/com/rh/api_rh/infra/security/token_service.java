package com.rh.api_rh.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.refreshToken.refresh_token_model;
import com.rh.api_rh.refreshToken.refresh_token_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class token_service {
    @Autowired
    private refresh_token_repository refresh_token_repository;

    @Value("${api.security.token.secret}")
    private String secret;
    @Autowired
    private com.rh.api_rh.refreshToken.refresh_token_service refresh_token_service;

    public String generateToken(funcionario_model funcionario) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Yuzo")
                    .withSubject(funcionario.getId().toString())
                    .withClaim("type", "funcionario")
                    .withExpiresAt(generateExpiration())
                    .sign(algorithm);
            return token;
        } catch (
                JWTCreationException e
        ) {
            throw new RuntimeException("Erro ao gerar token", e);
        }

    }

    public String generateTokenCandidato(candidato_model candidato) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Yuzo")
                    .withSubject(candidato.getId().toString())
                    .withClaim("type", "candidato")
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
            Optional<refresh_token_model> auxiliar = refresh_token_repository.findByRefreshtoken(token);
            if (auxiliar.isPresent()) {
                refresh_token_repository.delete(auxiliar.get());
            }
            return "";
        }
    }



    public Instant generateExpiration() {
        return LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.of("-03:00"));
    }

    public String returnClaim(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("Yuzo")
                    .build()
                    .verify(token);
            String type = decodedJWT.getClaim("type").asString();

            return type;

    }  catch (JWTVerificationException e) {
        return "";}
    }

}
