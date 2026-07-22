package com.piscicultura.inventario.negocio.dto;

import jakarta.validation.constraints.*;

public record ClienteRequest(
        @NotBlank String nombre,
        @NotBlank String tipoDocumento,
        String documento,
        String telefono,
        @Email String email,
        String direccion,
        Boolean activo
) {}
