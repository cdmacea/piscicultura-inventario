package com.piscicultura.inventario.inventario.mapper;

import com.piscicultura.inventario.inventario.dto.InsumoResponse;
import com.piscicultura.inventario.inventario.entity.Insumo;

/** Convierte entidades Insumo a su representación de respuesta. */
public final class InsumoMapper {

    private InsumoMapper() {}

    public static InsumoResponse toResponse(Insumo i) {
        boolean bajo = i.getStockActual() != null
                && i.getStockMinimo() != null
                && i.getStockActual().compareTo(i.getStockMinimo()) <= 0;
        return new InsumoResponse(
                i.getId(),
                i.getCodigo(),
                i.getNombre(),
                i.getCategoria() != null ? i.getCategoria().getId() : null,
                i.getCategoria() != null ? i.getCategoria().getNombre() : null,
                i.getProveedor() != null ? i.getProveedor().getId() : null,
                i.getProveedor() != null ? i.getProveedor().getNombre() : null,
                i.getUnidadMedida(),
                i.getStockActual(),
                i.getStockMinimo(),
                i.getPrecioUnitario(),
                bajo,
                i.getActivo()
        );
    }
}
