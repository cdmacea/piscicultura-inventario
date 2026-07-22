package com.piscicultura.inventario.common;

/**
 * Se lanza al violar una regla de negocio, p. ej. stock insuficiente (HTTP 409).
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
