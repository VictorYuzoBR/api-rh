package com.rh.api_rh.auth;

import com.rh.api_rh.DTO.LoginResponse_dto;
import com.rh.api_rh.DTO.login_dto;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.refreshToken.refresh_token_model;
import com.rh.api_rh.refreshToken.refresh_token_service;
import com.rh.api_rh.usuario.usuario_model;
import com.rh.api_rh.usuario.usuario_repository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.ToString;
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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private refresh_token_service refreshTokenService;

    @Autowired
    private usuario_repository usuarioRepository;

    @Value("${SALT_SECRETWORD:!Senhasecreta1}")
    private String salt_secret;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated login_dto dto) {

        Optional<usuario_model> datausuario = usuarioRepository.findByRegistro(dto.registro());
        if (datausuario.isPresent()) {
            usuario_model usuario = datausuario.get();
            if (usuario.getStatus().equals("BLOQUEADO")) {
                Date dataagora = new Date();
                Long diferenca = ((dataagora.getTime() - usuario.getDatabloqueio().getTime()) / 1000);
                if (diferenca >= 30) {
                    usuario.setStatus("ATIVO");
                    usuarioRepository.save(usuario);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario bloqueado temporariamente");
                }

            } else if (usuario.getStatus().equals("DESATIVADO")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario desativado");
            }
        }

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

                funcionario.get().getIdusuario().setTentativas(0);
                funcionarioRepository.save(funcionario.get());

                String role = funcionario.get().getCargo().toString();
                String email = funcionario.get().getEmail();
                var token = tokenService.generateToken(funcionario.get());
                var refreshtoken = refreshTokenService.generateRefreshToken(funcionario.get());
                String aceitoutermo = Boolean.toString(funcionario.get().getIdusuario().isAceitoutermos());
                String primeirologin = Boolean.toString(funcionario.get().getIdusuario().isPrimeirologin());

                Map<String, String> tokens = Map.of(
                        "access_token", token,
                        "refresh_token", refreshtoken,
                        "role", role,
                        "email", email,
                        "termo", aceitoutermo,
                        "primeirologin", primeirologin
                );

                return ResponseEntity.ok(tokens);
            }
            } catch (UsernameNotFoundException | BadCredentialsException e) {

                Optional<funcionario_model> funcionario = funcionarioRepository.findByIdusuario_Registro(dto.registro());
                if (funcionario.isPresent()) {
                    log_model log = new log_model();
                    log.setRegistro(dto.registro());
                    log.setAcao("Tentativa de login falha no usuário de registro "+dto.registro());
                    log.setData(new Date());
                    logRepository.save(log);

                    funcionario_model funcionarioparaalteracao = funcionario.get();
                    int tentativas = funcionarioparaalteracao.getIdusuario().getTentativas();
                    tentativas = tentativas + 1;
                    if (tentativas == 3) {
                        funcionarioparaalteracao.getIdusuario().setStatus("BLOQUEADO");
                        Date dataagora = new Date();
                        funcionarioparaalteracao.getIdusuario().setDatabloqueio(dataagora);
                    } else if (tentativas == 5) {
                        funcionarioparaalteracao.getIdusuario().setStatus("DESATIVADO");
                        Date dataagora = new Date();
                        funcionarioparaalteracao.getIdusuario().setDatabloqueio(dataagora);
                    }
                    funcionarioparaalteracao.getIdusuario().setTentativas(tentativas);
                    funcionarioRepository.save(funcionarioparaalteracao);

                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(tentativas);

                }

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario ou senha incorretos");

            }


        return ResponseEntity.badRequest().body("Erro inesperado no login");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request) {
        String token = recoverToken(request);
        if (token != null) {
            try {
                String newaccesstoken = refreshTokenService.validateRefreshToken(token);
                if (newaccesstoken == "") {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Seu refresh token expirou realize o login novamente");
                }
                return ResponseEntity.ok(newaccesstoken);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Erro inesperado no refresh");
            }
        } else {
            return ResponseEntity.badRequest().body("Erro inesperado no refresh");
        }

    }


    private String recoverToken(HttpServletRequest request) {

        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");

    }

    @GetMapping
    private List<refresh_token_model> listar() {
        return refreshTokenService.listar();
    }


}
