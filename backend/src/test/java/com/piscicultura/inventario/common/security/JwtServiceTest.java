package com.piscicultura.inventario.common.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private final JwtService jwtService =
            new JwtService("secreto-de-prueba-para-tests-minimo-32-caracteres", 60);

    @Test
    void generaUnTokenDelQueSePuedeRecuperarElUsuario() {
        String token = jwtService.generarToken("admin");

        assertThat(token).isNotBlank();
        assertThat(jwtService.extraerUsername(token)).isEqualTo("admin");
        assertThat(jwtService.esValido(token)).isTrue();
    }

    @Test
    void unTokenExpiradoNoEsValido() {
        JwtService serviceExpiraInstante = new JwtService(
                "secreto-de-prueba-para-tests-minimo-32-caracteres", 0);
        String token = serviceExpiraInstante.generarToken("admin");

        assertThat(serviceExpiraInstante.esValido(token)).isFalse();
    }

    @Test
    void unTokenManipuladoNoEsValido() {
        String token = jwtService.generarToken("admin");
        String manipulado = token.substring(0, token.length() - 2) + "xx";

        assertThat(jwtService.esValido(manipulado)).isFalse();
    }
}
