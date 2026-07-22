package com.piscicultura.inventario.dashboard;

import java.math.BigDecimal;

/** Indicadores generales de la finca para la pantalla principal. */
public record ResumenResponse(
        long totalInsumos,
        long insumosStockBajo,
        long lotesActivos,
        long pecesEnProduccion,
        BigDecimal ventasPagadas,
        long ventasPendientes
) {}
