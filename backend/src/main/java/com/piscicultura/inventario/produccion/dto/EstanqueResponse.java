package com.piscicultura.inventario.produccion.dto;

import java.math.BigDecimal;

public record EstanqueResponse(
        Long id,
        String codigo,
        String nombre,
        String tipo,
        BigDecimal areaM2,
        BigDecimal volumenM3,
        Integer capacidadMax,
        String estado
) {}
