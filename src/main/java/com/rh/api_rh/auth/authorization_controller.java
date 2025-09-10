package com.rh.api_rh.auth;

import com.rh.api_rh.DTO.login.loginFuncionario_dto;
import com.rh.api_rh.DTO.login.login_candidato_dto;
import com.rh.api_rh.DTO.login.refresh_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.infra.security.authTokens.funcionario_token;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.refreshToken.refresh_token_model;
import com.rh.api_rh.refreshToken.refresh_token_service;
import com.rh.api_rh.usuario.usuario_model;
import com.rh.api_rh.usuario.usuario_repository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class authorization_controller {

    @Autowired
    private authorization_service authorizationService;

    @Autowired
    private refresh_token_service refreshTokenService;



    @Value("${SALT_SECRETWORD:!Senhasecreta1}")
    private String salt_secret;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated loginFuncionario_dto dto) {

        try {
            return authorizationService.loginFuncionario(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody refresh_dto dto) {

        String refreshtoken = dto.getToken();

        if (refreshtoken != null) {
            try {
                String newaccesstoken = refreshTokenService.validateRefreshToken(refreshtoken);
                if (newaccesstoken == "") {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(refreshtoken);
                }
                return ResponseEntity.ok(newaccesstoken);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Erro inesperado no refresh");
            }
        } else {
            return ResponseEntity.badRequest().body("Erro inesperado no refresh");
        }

    }

    @PostMapping("/logincandidato")
    public ResponseEntity<?> loginCandidato(@RequestBody login_candidato_dto dto) {

        try {
            return authorizationService.loginCandidato(dto);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }




    @GetMapping
    private List<refresh_token_model> listar() {
        return refreshTokenService.listar();
    }


}
