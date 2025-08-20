package com.rh.api_rh.infra.security;

import com.rh.api_rh.infra.security.providers.candidato_provider;
import com.rh.api_rh.infra.security.providers.funcionario_provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class Security_config {


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            security_filter securityFilter
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/telefone").hasAnyRole("RH", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/funcionario").hasAnyRole("RH", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/log").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/funcionario").hasAnyRole("RH", "ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            funcionario_provider funcionarioProvider,
            candidato_provider candidatoProvider
    ) {
        return new ProviderManager(List.of(funcionarioProvider, candidatoProvider));
    }




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
