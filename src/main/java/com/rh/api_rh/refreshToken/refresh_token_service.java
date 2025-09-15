package com.rh.api_rh.refreshToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rh.api_rh.funcionario.funcionario_model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class refresh_token_service {

    @Autowired
    private refresh_token_repository refresh_token_repository;

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateRefreshToken(funcionario_model funcionario) {

        try {
            Optional<refresh_token_model> auxiliar = refresh_token_repository.findByIdusuario_Id(funcionario.getIdusuario().getId());
            if (auxiliar.isPresent()) {
                refresh_token_repository.delete(auxiliar.get());
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Yuzo")
                    .withSubject(funcionario.getId().toString())
                    .withExpiresAt(generateExpiration())
                    .sign(algorithm);

            refresh_token_model refreshtoken = new refresh_token_model();
            refreshtoken.setRefreshtoken(token);
            refreshtoken.setIdusuario(funcionario.getIdusuario());
            refresh_token_repository.save(refreshtoken);

            return token;

        } catch (
                JWTCreationException e
        ) {
            throw new RuntimeException("Erro ao gerar token", e);
        }

    }

    public String validateRefreshToken(String token) {
        Optional<refresh_token_model> data = refresh_token_repository.findByRefreshtoken(token);
        if (data.isPresent()) {
            refresh_token_model refreshtoken = data.get();
            try {
                Algorithm algorithm = Algorithm.HMAC256(secret);



                    DecodedJWT decoded = JWT.require(algorithm)
                            .withIssuer("Yuzo")
                            .build()
                            .verify(token);

                    String idfuncionario = decoded.getSubject();


                    String newtoken = JWT.create()
                            .withIssuer("Yuzo")
                            .withSubject(idfuncionario)
                            .withClaim("type", "funcionario")
                            .withExpiresAt(generateAccessExpiration())
                            .sign(algorithm);

                    return newtoken;

                } catch (JWTVerificationException e) {

                refresh_token_repository.delete(refreshtoken);
                return "";

                }
            }
        else {
            return "";
        }
    }

    public List<refresh_token_model> listar() {
        return refresh_token_repository.findAll();
    }



    public Instant generateExpiration() {
        return LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.of("-03:00"));
    }

    public Instant generateAccessExpiration() {
        return LocalDateTime.now().plusMinutes(1).toInstant(ZoneOffset.of("-03:00"));
    }



}
