package com.piscicultura.inventario.negocio.dto;

public record ClienteResponse(
        Long id,
        String nombre,
        String tipoDocumento,
        String documento,
        String telefono,
        String email,
        String direccion,
        Boolean activo
) {}
