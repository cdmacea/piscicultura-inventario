package com.piscicultura.inventario.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * Emite y valida los JWT usados para autenticar contra la API.
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMinutes;

    public JwtService(@Value("${app.security.jwt-secret}") String secret,
                       @Value("${app.security.jwt-expiration-minutes}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMinutes = expirationMinutes;
    }

    public String generarToken(String username) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(expirationMinutes * 60)))
                .signWith(key)
                .compact();
    }

    public String extraerUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean esValido(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
