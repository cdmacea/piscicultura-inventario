package com.piscicultura.inventario.negocio.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record VentaRequest(
        @NotBlank String codigo,
        @NotNull Long clienteId,
        LocalDate fecha,
        String metodoPago,
        @NotEmpty @Valid List<DetalleVentaDTO> detalles
) {}
