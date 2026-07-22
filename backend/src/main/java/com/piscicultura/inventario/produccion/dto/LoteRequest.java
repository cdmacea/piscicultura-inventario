package com.piscicultura.inventario.produccion.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoteRequest(
        @NotBlank String codigo,
        @NotNull Long especieId,
        @NotNull Long estanqueId,
        @NotNull LocalDate fechaSiembra,
        @NotNull @Positive Integer cantidadInicial,
        @PositiveOrZero Integer cantidadActual,
        @PositiveOrZero BigDecimal pesoPromedioG,
        String estado,
        String observaciones
) {}
