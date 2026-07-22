package com.piscicultura.inventario.produccion.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record EstanqueRequest(
        @NotBlank String codigo,
        @NotBlank String nombre,
        @NotBlank String tipo,
        @PositiveOrZero BigDecimal areaM2,
        @PositiveOrZero BigDecimal volumenM3,
        @PositiveOrZero Integer capacidadMax,
        String estado
) {}
