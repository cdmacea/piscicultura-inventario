package com.piscicultura.inventario.common;

/**
 * Se lanza cuando no existe un recurso solicitado (HTTP 404).
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    public RecursoNoEncontradoException(String recurso, Object id) {
        super(recurso + " con id " + id + " no encontrado");
    }
}
