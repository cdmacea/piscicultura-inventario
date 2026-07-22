package com.piscicultura.inventario.inventario.mapper;

import com.piscicultura.inventario.inventario.dto.MovimientoResponse;
import com.piscicultura.inventario.inventario.entity.MovimientoInventario;

public final class MovimientoMapper {

    private MovimientoMapper() {}

    public static MovimientoResponse toResponse(MovimientoInventario m) {
        return new MovimientoResponse(
                m.getId(),
                m.getInsumo().getId(),
                m.getInsumo().getNombre(),
                m.getTipo(),
                m.getCantidad(),
                m.getStockResultante(),
                m.getMotivo(),
                m.getUsuario(),
                m.getFecha()
        );
    }
}
