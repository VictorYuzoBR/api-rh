package com.rh.api_rh.auth;

import com.rh.api_rh.DTO.login.loginFuncionario_dto;
import com.rh.api_rh.DTO.login.loginResponseCandidato_dto;
import com.rh.api_rh.DTO.login.loginResponse_dto;
import com.rh.api_rh.DTO.login.login_candidato_dto;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import com.rh.api_rh.infra.security.authTokens.candidato_token;
import com.rh.api_rh.infra.security.authTokens.funcionario_token;
import com.rh.api_rh.infra.security.token_service;
import com.rh.api_rh.log.log_model;
import com.rh.api_rh.log.log_repository;
import com.rh.api_rh.refreshToken.refresh_token_service;
import com.rh.api_rh.usuario.usuario_model;
import com.rh.api_rh.usuario.usuario_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class authorization_service {

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

    @Autowired
    private candidato_repository candidatoRepository;

    @Value("${SALT_SECRETWORD:!Senhasecreta1}")
    private String salt_secret;


    public ResponseEntity<?> loginFuncionario(loginFuncionario_dto dto) {

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

        var usernamePassword = new funcionario_token(dto.registro(), salt);

        try{
            var auth = this.authenticationManager.authenticate(usernamePassword);

            UserDetails userDetails = (User) auth.getPrincipal();

            Optional<funcionario_model> funcionario = funcionarioRepository.findByIdusuario_Registro(userDetails.getUsername());
            if (funcionario.isPresent()) {

                log_model log = new log_model();
                log.setRegistro(dto.registro());
                log.setAcao("Tentativa de login bem sucedida no usuário de registro "+dto.registro());
                log.setData(new Date());
                log.setTipo("funcionario");
                logRepository.save(log);

                funcionario.get().getIdusuario().setTentativas(0);
                funcionarioRepository.save(funcionario.get());

                String role = funcionario.get().getCargo().toString();
                String email = funcionario.get().getEmail();
                var token = tokenService.generateToken(funcionario.get());
                var refreshtoken = refreshTokenService.generateRefreshToken(funcionario.get());
                String aceitoutermo = Boolean.toString(funcionario.get().getIdusuario().isAceitoutermos());
                String primeirologin = Boolean.toString(funcionario.get().getIdusuario().isPrimeirologin());

                loginResponse_dto response = new loginResponse_dto();

                response.setRole(role);
                response.setEmail(email);
                response.setTermo(aceitoutermo);
                response.setPrimeiro_login(primeirologin);
                response.setAccess_token(token);
                response.setRefresh_token(refreshtoken);

                return ResponseEntity.ok(response);
            }
        } catch (UsernameNotFoundException | BadCredentialsException e) {

            Optional<funcionario_model> funcionario = funcionarioRepository.findByIdusuario_Registro(dto.registro());
            if (funcionario.isPresent()) {
                log_model log = new log_model();
                log.setRegistro(dto.registro());
                log.setAcao("Tentativa de login falha no usuário de registro "+dto.registro());
                log.setData(new Date());
                log.setTipo("funcionario");
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


    public ResponseEntity<?> loginCandidato(login_candidato_dto dto) {

        String salt = dto.getEmail() + dto.getPassword() + salt_secret;

        var usernamePassword = new candidato_token(dto.getEmail(), salt);

        Optional<candidato_model> candidato_model = candidatoRepository.findByEmail(dto.getEmail());
        if (candidato_model.isPresent()) {
            candidato_model candidato = candidato_model.get();

            if (candidato.getStatus().equals("BLOQUEADO")) {
                Date dataagora = new Date();
                Long diferenca = ((dataagora.getTime() - candidato.getDatabloqueio().getTime()) / 1000);
                if (diferenca >= 30) {
                    candidato.setStatus("ATIVO");
                    candidatoRepository.save(candidato);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("candidato bloqueado temporariamente");
                }

            } else if (candidato.getStatus().equals("DESATIVADO")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("candidato desativado");
            }
        }


        try {
            var auth = this.authenticationManager.authenticate(usernamePassword);

            UserDetails userDetails = (User) auth.getPrincipal();

            Optional<candidato_model> candidato_model2 = candidatoRepository.findByEmail(userDetails.getUsername());

            if (candidato_model2.isPresent()) {

                candidato_model candidato = candidato_model2.get();


                log_model log = new log_model();
                log.setRegistro(String.valueOf(candidato.getId()));
                log.setAcao("Tentativa de login bem sucedida no candidato de id "+candidato.getId());
                log.setData(new Date());
                log.setTipo("candidato");
                logRepository.save(log);

                candidato.setTentativas(0);
                candidatoRepository.save(candidato);


                loginResponseCandidato_dto response = new loginResponseCandidato_dto();

                String termo = String.valueOf(candidato_model.get().getAceitouTermo());
                var token = tokenService.generateTokenCandidato(candidato_model.get());
                String id = Long.toString(candidato.getId());

                response.setAccess_token(token);
                response.setTermo(termo);
                response.setId(id);
                return ResponseEntity.ok(response);

            }

        } catch (UsernameNotFoundException | BadCredentialsException e) {



            Optional<candidato_model> candidato_model3 = candidatoRepository.findByEmail(dto.getEmail());

            if (candidato_model3.isPresent()) {

                candidato_model candidato = candidato_model3.get();

                log_model log = new log_model();
                log.setRegistro(String.valueOf(candidato.getId()));
                log.setAcao("Tentativa de login falha no candidato de id "+candidato.getId());
                log.setData(new Date());
                log.setTipo("funcionario");
                logRepository.save(log);

                int tentativas = candidato.getTentativas();
                tentativas = tentativas + 1;
                if (tentativas == 3) {
                    candidato.setStatus("BLOQUEADO");
                    Date dataagora = new Date();
                    candidato.setDatabloqueio(dataagora);
                } else if (tentativas == 5) {
                    candidato.setStatus("DESATIVADO");
                    Date dataagora = new Date();
                    candidato.setDatabloqueio(dataagora);
                }
                candidato.setTentativas(tentativas);
                candidatoRepository.save(candidato);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(tentativas);

            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario ou senha incorretos");

            }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("erro inesperado no login");

    }

}
