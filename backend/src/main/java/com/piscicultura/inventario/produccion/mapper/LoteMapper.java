package com.piscicultura.inventario.produccion.mapper;

import com.piscicultura.inventario.produccion.dto.LoteResponse;
import com.piscicultura.inventario.produccion.entity.Lote;

public final class LoteMapper {

    private LoteMapper() {}

    public static LoteResponse toResponse(Lote l) {
        return new LoteResponse(
                l.getId(), l.getCodigo(),
                l.getEspecie().getId(), l.getEspecie().getNombreComun(),
                l.getEstanque().getId(), l.getEstanque().getNombre(),
                l.getFechaSiembra(), l.getCantidadInicial(), l.getCantidadActual(),
                l.getPesoPromedioG(), l.getEstado(), l.getObservaciones());
    }
}
