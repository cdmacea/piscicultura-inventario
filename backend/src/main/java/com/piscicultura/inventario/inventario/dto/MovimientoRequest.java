package com.piscicultura.inventario.inventario.dto;

import com.piscicultura.inventario.inventario.entity.TipoMovimiento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** Datos para registrar un movimiento de stock. */
public record MovimientoRequest(
        @NotNull Long insumoId,
        @NotNull TipoMovimiento tipo,
        @NotNull @Positive BigDecimal cantidad,
        String motivo,
        String usuario
) {}
