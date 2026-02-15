package com.SIMOD.SIMOD.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
Esta classe define a configuração completa de segurança da aplicação, integrando autenticação via JWT,
controle de acesso por papéis e uma arquitetura stateless, garantindo que cada requisição seja validada
de forma segura e consistente com as regras de negócio.
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    // Define o algoritmo de hash utilizado para armazenar e validar senhas de forma segura.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    // Regras de autorização por endpoint
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/pacientes/**").hasRole("PACIENTE")
                        .requestMatchers("/cuidadores/**").hasRole("CUIDADOR")
                        .requestMatchers("/profissionais/**").hasAnyRole("MEDICO",
                                "NUTRICIONISTA", "FISIOTERAPEUTA", "FONOAUDIOLOGO", "PSICOLOGO")
                        .requestMatchers("/mensagens/**").authenticated()
                        .requestMatchers("/sessoes/profissionais/**").hasAnyRole("MEDICO",
                                "NUTRICIONISTA", "FISIOTERAPEUTA", "FONOAUDIOLOGO", "PSICOLOGO")
                        .requestMatchers("/sessoes/cuidaores/**").hasRole("CUIDADOR")
                        .requestMatchers("/sessoes/pacientes/**").hasRole("PACIENTE")
                        .requestMatchers("/dietas/nutricionistas/**").hasRole("NUTRICIONISTA")
                        .requestMatchers("/dietas/usuarios/**").authenticated()
                        .requestMatchers("/medicamentos/medicos/**").hasRole("MEDICO")
                        .requestMatchers("/medicamentos/usuarios/**").authenticated()
                        .requestMatchers("/atividades/fisio-fono/**").hasAnyRole("FISIOTERAPEUTA",
                                "FONOAUDIOLOGO")
                        .requestMatchers("/atividades/usuarios/**").authenticated()
                        .requestMatchers("/diario-de-saude/**").authenticated()
                        .requestMatchers("/diario-de-saude/caregiver/**").hasRole("CUIDADOR")
                        .requestMatchers("/diario-de-saude/professional/**").hasAnyRole("MEDICO",
                                "NUTRICIONISTA", "FISIOTERAPEUTA", "FONOAUDIOLOGO", "PSICOLOGO")
                        .requestMatchers("/diario-de-saude/listarDiarios/**").hasAnyRole("PACIENTE", "CUIDADOR")
                        .requestMatchers("/diario-de-saude/confirmar/**").hasAnyRole("PACIENTE", "CUIDADOR")
                        .anyRequest().authenticated()
                )
                // Indica que a aplicação não mantém estado de sessão, reforçando o uso exclusivo do JWT para autenticação.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}