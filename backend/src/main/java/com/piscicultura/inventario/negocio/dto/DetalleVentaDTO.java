package com.piscicultura.inventario.negocio.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record DetalleVentaDTO(
        Long loteId,
        @NotBlank String descripcion,
        @NotNull @Positive BigDecimal cantidadKg,
        @NotNull @PositiveOrZero BigDecimal precioKg
) {}
