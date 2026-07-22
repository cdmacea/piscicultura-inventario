package com.piscicultura.inventario.produccion.mapper;

import com.piscicultura.inventario.produccion.dto.EstanqueResponse;
import com.piscicultura.inventario.produccion.entity.Estanque;

public final class EstanqueMapper {

    private EstanqueMapper() {}

    public static EstanqueResponse toResponse(Estanque e) {
        return new EstanqueResponse(
                e.getId(), e.getCodigo(), e.getNombre(), e.getTipo(),
                e.getAreaM2(), e.getVolumenM3(), e.getCapacidadMax(), e.getEstado());
    }
}
