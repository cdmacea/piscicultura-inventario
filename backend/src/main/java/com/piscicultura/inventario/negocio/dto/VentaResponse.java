package com.piscicultura.inventario.negocio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VentaResponse(
        Long id,
        String codigo,
        Long clienteId,
        String clienteNombre,
        LocalDate fecha,
        BigDecimal total,
        String estado,
        String metodoPago,
        List<DetalleVentaResponse> detalles
) {
    public record DetalleVentaResponse(
            Long id,
            Long loteId,
            String descripcion,
            BigDecimal cantidadKg,
            BigDecimal precioKg,
            BigDecimal subtotal
    ) {}
}
