package com.rh.api_rh.infra.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.infra.security.authTokens.candidato_token;
import com.rh.api_rh.infra.security.authTokens.funcionario_token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class security_filter extends OncePerRequestFilter {

    private final token_service tokenService;
    private final AuthenticationManager authenticationManager;
    private final funcionario_repository funcionarioRepository;
    private final candidato_repository candidatoRepository;

    public security_filter(token_service tokenService, AuthenticationManager authenticationManager, funcionario_repository funcionarioRepository, candidato_repository candidatoRepository) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.funcionarioRepository = funcionarioRepository;
        this.candidatoRepository = candidatoRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {
            String subject = tokenService.validateToken(token);
            if (subject.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Seu token expirou ou é inválido.");
                return;
            }

         
            String type = tokenService.returnClaim(token);


            if (type.equals("funcionario")) {

                Optional<funcionario_model> aux = funcionarioRepository.findById(UUID.fromString(subject));

                if (aux.isPresent()) {
                    funcionario_model funcionario = aux.get();
                    var authentication = new funcionario_token(
                            funcionario.getUsername(),
                            null,
                            funcionario.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }




            } else if (type.equals("candidato")) {

                System.out.println("chegou aqui");

                Long id = Long.parseLong(subject);

                System.out.println(id);
                Optional<candidato_model> aux = candidatoRepository.findById(id);

                if  (aux.isPresent()) {
                    candidato_model candidato = aux.get();

                    var authentication = new candidato_token(
                            candidato.getUsername(),
                            null,
                            candidato.getAuthorities()
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }


        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}