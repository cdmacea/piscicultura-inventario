package com.piscicultura.inventario.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Separado de SecurityConfig para evitar un ciclo: SecurityConfig depende de
 * JwtAuthFilter, que depende de AppUserDetailsService, que depende del
 * PasswordEncoder (si viviera dentro de SecurityConfig, su propia creación
 * quedaría bloqueada esperando a sí misma).
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
