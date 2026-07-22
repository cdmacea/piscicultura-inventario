package com.piscicultura.inventario.common;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura estándar de respuesta de error.
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validaciones
) {
    public ApiError(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }
}
