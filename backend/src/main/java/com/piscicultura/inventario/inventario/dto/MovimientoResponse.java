package com.piscicultura.inventario.inventario.dto;

import com.piscicultura.inventario.inventario.entity.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        Long insumoId,
        String insumoNombre,
        TipoMovimiento tipo,
        BigDecimal cantidad,
        BigDecimal stockResultante,
        String motivo,
        String usuario,
        LocalDateTime fecha
) {}
