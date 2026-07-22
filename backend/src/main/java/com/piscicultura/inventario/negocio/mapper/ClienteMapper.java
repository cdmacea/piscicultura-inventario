package com.piscicultura.inventario.negocio.mapper;

import com.piscicultura.inventario.negocio.dto.ClienteResponse;
import com.piscicultura.inventario.negocio.entity.Cliente;

public final class ClienteMapper {

    private ClienteMapper() {}

    public static ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(
                c.getId(), c.getNombre(), c.getTipoDocumento(), c.getDocumento(),
                c.getTelefono(), c.getEmail(), c.getDireccion(), c.getActivo());
    }
}
