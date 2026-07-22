package com.piscicultura.inventario.inventario.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** Datos para crear o actualizar un insumo. */
public record InsumoRequest(
        @NotBlank String codigo,
        @NotBlank String nombre,
        @NotNull Long categoriaId,
        Long proveedorId,
        @NotBlank String unidadMedida,
        @NotNull @PositiveOrZero BigDecimal stockMinimo,
        @NotNull @PositiveOrZero BigDecimal precioUnitario,
        @PositiveOrZero BigDecimal stockInicial,
        Boolean activo
) {}
