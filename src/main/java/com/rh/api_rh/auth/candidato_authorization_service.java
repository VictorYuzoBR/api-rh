package com.rh.api_rh.auth;

import com.rh.api_rh.DTO.login.login_candidato_dto;
import com.rh.api_rh.candidato.candidato_model;
import com.rh.api_rh.candidato.candidato_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class candidato_authorization_service implements UserDetailsService {

    @Autowired
    private candidato_repository candidatorepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        candidato_model candidato = candidatorepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Candidato não encontrado com email: " + email));

        return new User(
                candidato.getUsername(),
                candidato.getPassword(),
                candidato.getAuthorities()
        );
    }



}
