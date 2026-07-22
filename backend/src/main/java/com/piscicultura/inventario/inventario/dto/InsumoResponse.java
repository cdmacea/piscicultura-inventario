package com.piscicultura.inventario.inventario.dto;

import java.math.BigDecimal;

/** Representación de un insumo devuelta al frontend. */
public record InsumoResponse(
        Long id,
        String codigo,
        String nombre,
        Long categoriaId,
        String categoriaNombre,
        Long proveedorId,
        String proveedorNombre,
        String unidadMedida,
        BigDecimal stockActual,
        BigDecimal stockMinimo,
        BigDecimal precioUnitario,
        boolean stockBajo,
        Boolean activo
) {}
