package com.piscicultura.inventario.common.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * No hay gestión de usuarios en base de datos: la aplicación la usa un único
 * responsable (la finca), así que el usuario admin se configura por propiedades.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final String adminUsername;
    private final String encodedPassword;

    public AppUserDetailsService(@Value("${app.security.admin-username}") String adminUsername,
                                  @Value("${app.security.admin-password}") String adminPassword,
                                  PasswordEncoder passwordEncoder) {
        this.adminUsername = adminUsername;
        this.encodedPassword = passwordEncoder.encode(adminPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!adminUsername.equals(username)) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        // Se construye una instancia NUEVA en cada llamada: Spring Security borra
        // las credenciales del UserDetails autenticado tras cada login exitoso
        // (eraseCredentialsAfterAuthentication). Si se reutilizara una instancia
        // cacheada, el segundo intento de login siempre fallaría porque la
        // contraseña ya habría quedado en null.
        return User.withUsername(adminUsername)
                .password(encodedPassword)
                .roles("ADMIN")
                .build();
    }
}
