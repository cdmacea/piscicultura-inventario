package com.piscicultura.inventario.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica de punta a punta que la API queda protegida y que el login emite
 * un JWT utilizable. Requiere la base de datos local "piscicultura" activa
 * (misma que usa la aplicación en desarrollo, ver /database o application.yml).
 *
 * Necesita Apache HttpClient5 en el classpath de test (ver pom.xml): con el
 * cliente JDK por defecto, HttpURLConnection no puede releer el cuerpo de un
 * POST cuando el servidor responde 401.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void endpointProtegidoRechazaPeticionesSinToken() {
        ResponseEntity<String> resp = rest.getForEntity("/inventario/insumos", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void loginConCredencialesIncorrectasDevuelve401() {
        var body = new LoginRequest("admin", "clave-incorrecta");
        ResponseEntity<String> resp = rest.postForEntity("/auth/login", body, String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void loginValidoDevuelveUnTokenQueDaAccesoALaApi() {
        var body = new LoginRequest("admin", "changeme-local-dev");
        ResponseEntity<LoginResponse> loginResp = rest.postForEntity("/auth/login", body, LoginResponse.class);

        assertThat(loginResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResp.getBody()).isNotNull();
        String token = loginResp.getBody().token();
        assertThat(token).isNotBlank();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        ResponseEntity<String> protegido = rest.exchange(
                "/inventario/insumos", HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertThat(protegido.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void sePuedeIniciarSesionVariasVecesSeguidas() {
        // Regresión: AppUserDetailsService devolvía un mismo UserDetails cacheado;
        // Spring Security borra sus credenciales tras el primer login exitoso
        // (eraseCredentialsAfterAuthentication), así que el segundo intento con
        // las MISMAS credenciales fallaba siempre. Ver commit que corrige esto.
        var body = new LoginRequest("admin", "changeme-local-dev");

        ResponseEntity<LoginResponse> primero = rest.postForEntity("/auth/login", body, LoginResponse.class);
        assertThat(primero.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<LoginResponse> segundo = rest.postForEntity("/auth/login", body, LoginResponse.class);
        assertThat(segundo.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<LoginResponse> tercero = rest.postForEntity("/auth/login", body, LoginResponse.class);
        assertThat(tercero.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
