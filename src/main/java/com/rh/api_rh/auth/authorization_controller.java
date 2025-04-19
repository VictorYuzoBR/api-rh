package com.rh.api_rh.auth;

import com.rh.api_rh.DTO.LoginResponse_dto;
import com.rh.api_rh.DTO.login_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.infra.security.token_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class authorization_controller {

    @Autowired
    private token_service tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private funcionario_repository funcionarioRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated login_dto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.registro(), dto.senha());


            var auth = this.authenticationManager.authenticate(usernamePassword);

            UserDetails userDetails = (User) auth.getPrincipal();

            Optional<funcionario_model> funcionario = funcionarioRepository.findByIdusuario_Registro(userDetails.getUsername());
            if (funcionario.isPresent()) {
                var token = tokenService.generateToken(funcionario.get());
                return ResponseEntity.ok(token);
            }
            

        return ResponseEntity.badRequest().body("Usuário ou senha incorretos");
    }

}
