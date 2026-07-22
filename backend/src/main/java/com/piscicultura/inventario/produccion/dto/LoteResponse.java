package com.piscicultura.inventario.produccion.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoteResponse(
        Long id,
        String codigo,
        Long especieId,
        String especieNombre,
        Long estanqueId,
        String estanqueNombre,
        LocalDate fechaSiembra,
        Integer cantidadInicial,
        Integer cantidadActual,
        BigDecimal pesoPromedioG,
        String estado,
        String observaciones
) {}
