package com.piscicultura.inventario.common;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Traduce excepciones a respuestas HTTP consistentes para el frontend.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> noEncontrado(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req);
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> reglaNegocio(ReglaNegocioException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> credencialesInvalidas(AuthenticationException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errores = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errores.put(fe.getField(), fe.getDefaultMessage());
        }
        ApiError body = new ApiError(java.time.LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), "Bad Request",
                "Errores de validación", req.getRequestURI(), errores);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> generico(Exception ex, HttpServletRequest req) {
        // No se expone ex.getMessage() al cliente: puede contener detalles internos
        // (rutas, SQL, nombres de clase). Se registra completo en el log del servidor.
        log.error("Error no controlado en {}", req.getRequestURI(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado. Intenta de nuevo.", req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String msg, HttpServletRequest req) {
        ApiError body = new ApiError(status.value(), status.getReasonPhrase(), msg, req.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
