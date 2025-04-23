package com.rh.api_rh.auth;

import com.rh.api_rh.DTO.LoginResponse_dto;
import com.rh.api_rh.DTO.login_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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

    @Autowired
    private log_repository logRepository;

    @Value("${SALT_SECRETWORD:!Senhasecreta1}")
    private String salt_secret;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated login_dto dto) {

        String salt = dto.registro() + dto.senha() + salt_secret;

        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.registro(), salt);

            try{
            var auth = this.authenticationManager.authenticate(usernamePassword);

            UserDetails userDetails = (User) auth.getPrincipal();

            Optional<funcionario_model> funcionario = funcionarioRepository.findByIdusuario_Registro(userDetails.getUsername());
            if (funcionario.isPresent()) {

                log_model log = new log_model();
                log.setRegistro(dto.registro());
                log.setAcao("Tentativa de login bem sucedida no usuário de registro "+dto.registro());
                log.setData(new Date());
                logRepository.save(log);


                var token = tokenService.generateToken(funcionario.get());
                return ResponseEntity.ok(token);
            }
            } catch (UsernameNotFoundException | BadCredentialsException e) {

                Optional<funcionario_model> funcionario = funcionarioRepository.findByIdusuario_Registro(dto.registro());
                if (funcionario.isPresent()) {
                    log_model log = new log_model();
                    log.setRegistro(dto.registro());
                    log.setAcao("Tentativa de login falha no usuário de registro "+dto.registro());
                    log.setData(new Date());
                    logRepository.save(log);

                }

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario ou senha incorretos");

            }


        return ResponseEntity.badRequest().body("Erro inesperado no login");
    }

}
