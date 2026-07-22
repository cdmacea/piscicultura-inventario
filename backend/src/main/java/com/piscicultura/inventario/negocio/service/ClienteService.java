package com.piscicultura.inventario.negocio.service;

import com.piscicultura.inventario.common.RecursoNoEncontradoException;
import com.piscicultura.inventario.negocio.dto.ClienteRequest;
import com.piscicultura.inventario.negocio.dto.ClienteResponse;
import com.piscicultura.inventario.negocio.entity.Cliente;
import com.piscicultura.inventario.negocio.mapper.ClienteMapper;
import com.piscicultura.inventario.negocio.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return repo.findAll().stream().map(ClienteMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtener(Long id) {
        return ClienteMapper.toResponse(buscar(id));
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest req) {
        Cliente c = Cliente.builder()
                .nombre(req.nombre()).tipoDocumento(req.tipoDocumento())
                .documento(req.documento()).telefono(req.telefono())
                .email(req.email()).direccion(req.direccion())
                .activo(req.activo() != null ? req.activo() : true)
                .build();
        return ClienteMapper.toResponse(repo.save(c));
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest req) {
        Cliente c = buscar(id);
        c.setNombre(req.nombre());
        c.setTipoDocumento(req.tipoDocumento());
        c.setDocumento(req.documento());
        c.setTelefono(req.telefono());
        c.setEmail(req.email());
        c.setDireccion(req.direccion());
        if (req.activo() != null) c.setActivo(req.activo());
        return ClienteMapper.toResponse(repo.save(c));
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente c = buscar(id);
        c.setActivo(false);
        repo.save(c);
    }

    private Cliente buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
    }
}
