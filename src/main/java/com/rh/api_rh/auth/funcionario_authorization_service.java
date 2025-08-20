package com.rh.api_rh.auth;


import com.rh.api_rh.funcionario.funcionario_model;
import com.rh.api_rh.funcionario.funcionario_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class funcionario_authorization_service implements UserDetailsService {

    @Autowired
    funcionario_repository funcionario_repository;

    @Override
    public UserDetails loadUserByUsername(String registro) throws UsernameNotFoundException {
        funcionario_model funcionario = funcionario_repository.findByIdusuario_Registro(registro)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return new User(
                funcionario.getIdusuario().getRegistro(),
                funcionario.getIdusuario().getSenha(),
                funcionario.getAuthorities() 
        );
    }
}
