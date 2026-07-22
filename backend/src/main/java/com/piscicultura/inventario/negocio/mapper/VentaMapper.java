package com.piscicultura.inventario.negocio.mapper;

import com.piscicultura.inventario.negocio.dto.VentaResponse;
import com.piscicultura.inventario.negocio.entity.Venta;

import java.util.List;

public final class VentaMapper {

    private VentaMapper() {}

    public static VentaResponse toResponse(Venta v) {
        List<VentaResponse.DetalleVentaResponse> det = v.getDetalles().stream()
                .map(d -> new VentaResponse.DetalleVentaResponse(
                        d.getId(), d.getLoteId(), d.getDescripcion(),
                        d.getCantidadKg(), d.getPrecioKg(), d.getSubtotal()))
                .toList();
        return new VentaResponse(
                v.getId(), v.getCodigo(),
                v.getCliente().getId(), v.getCliente().getNombre(),
                v.getFecha(), v.getTotal(), v.getEstado().name(),
                v.getMetodoPago(), det);
    }
}
