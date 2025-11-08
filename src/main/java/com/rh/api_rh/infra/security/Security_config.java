package com.rh.api_rh.infra.security;

import com.rh.api_rh.infra.security.providers.candidato_provider;
import com.rh.api_rh.infra.security.providers.funcionario_provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class Security_config {


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            security_filter securityFilter
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        /*
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logincandidato").permitAll()
                        .requestMatchers(HttpMethod.POST, "/codigosenhacandidato").permitAll()
                        .requestMatchers(HttpMethod.POST, "/codigosenhacandidato/validar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/confirmaremail").permitAll()
                        .requestMatchers(HttpMethod.POST, "/confirmaremail/validar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/habilidade").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/habilidade").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/idioma").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/idioma").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/vaga").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/vaga").authenticated()
                        .requestMatchers(HttpMethod.POST, "/vaga/candidatura").authenticated()
                        .requestMatchers(HttpMethod.GET, "/vaga/teste/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/vaga/etapas/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/vaga/melhores/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/vaga/listaCompatibilidades/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/vaga/calcularCompatibilidade").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/vaga/avancarEtapa").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/vaga/desistencia").authenticated()
                        .requestMatchers(HttpMethod.POST, "/vaga/finalizarAplicacao").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/vaga/finalizarVaga").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET,"/vaga/listarVagasAtivas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/vaga/listarPorUmaEtapa").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/candidato").permitAll()
                        .requestMatchers(HttpMethod.GET, "/candidato").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/candidato/filtrar").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST,"/candidato/enviarEmailNovaVaga").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST,"/candidato/aceitartermo/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/candidato/perfil/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/candidato/atualizar").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/candidato/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/candidato/buscarCandidaturas/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/candidato/trocarSenhaCandidato").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comunicado").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/comunicado/funcionario/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/comunicado/buscar/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST,"/comunicado/alterarVisto").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/comunicado").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/espelhoItem/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/espelho").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/espelho/baterponto/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/espelho/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST, "/espelho/abono").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/espelho/gerarFeriado").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/espelho/espelhoDoMes/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.POST,"/espelho/gerarPDF").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/espelho/data").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.PUT,"/espelho/atestado").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST,"/ferias").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.PUT,"/ferias").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/ferias/feriasporsetor/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET,"/ferias/funcionario/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET,"/ferias/feriasConflitantes/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET,"/ferias)").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST,"/ferias/testelogica").permitAll()
                        .requestMatchers(HttpMethod.GET,"/ferias/gerarEspelhosFalsos").permitAll()
                        .requestMatchers(HttpMethod.GET,"/ferias/calcularFeriasFalso").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/ferias/venderferias").permitAll()
                        .requestMatchers(HttpMethod.POST,"/endereco").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET,"/endereco").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST,"/telefone").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET,"/telefone").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/funcionario").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/funcionario").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/funcinario/**").hasAnyRole("ADMIN", "RH","FUNCIONARIO")
                        .requestMatchers(HttpMethod.GET, "/funcionario/buscarporcargo/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.DELETE, "/funcionario/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.PUT, "/funcionario").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.PUT, "/funcionario/aceitartermo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/funcionario/generateadmin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/funcionario/buscarParaEnviarComunicadoSetor").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/funcionario/buscarParaEnviarComunicadoNome").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/funcionario/buscarParaEnviarComunicadoFuncao").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/funcionario/filaexclusao").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/funcionario/atualizarEndereco").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/log").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.POST, "/setor").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/setor").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.GET, "/setor/**").hasAnyRole("ADMIN", "RH")
                        .requestMatchers(HttpMethod.DELETE, "/setor/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/usuario/novasenha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/codigosenha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/codigosenha/validar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/codigosenhacandidato").permitAll()
                        .requestMatchers(HttpMethod.POST, "/codigosenhacandidato/validar").permitAll()

                         */
                        .anyRequest().permitAll()

                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();// Replace with your frontend origin
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://bikube-frontend.vercel.app/"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
