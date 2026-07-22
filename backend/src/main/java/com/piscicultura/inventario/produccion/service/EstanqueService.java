package com.piscicultura.inventario.produccion.service;

import com.piscicultura.inventario.common.RecursoNoEncontradoException;
import com.piscicultura.inventario.common.ReglaNegocioException;
import com.piscicultura.inventario.produccion.dto.EstanqueRequest;
import com.piscicultura.inventario.produccion.dto.EstanqueResponse;
import com.piscicultura.inventario.produccion.entity.Estanque;
import com.piscicultura.inventario.produccion.mapper.EstanqueMapper;
import com.piscicultura.inventario.produccion.repository.EstanqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstanqueService {

    private final EstanqueRepository repo;

    public EstanqueService(EstanqueRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<EstanqueResponse> listar() {
        return repo.findAll().stream().map(EstanqueMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EstanqueResponse obtener(Long id) {
        return EstanqueMapper.toResponse(buscar(id));
    }

    @Transactional
    public EstanqueResponse crear(EstanqueRequest req) {
        if (repo.existsByCodigo(req.codigo())) {
            throw new ReglaNegocioException("Ya existe un estanque con el código " + req.codigo());
        }
        Estanque e = Estanque.builder()
                .codigo(req.codigo()).nombre(req.nombre()).tipo(req.tipo())
                .areaM2(req.areaM2()).volumenM3(req.volumenM3())
                .capacidadMax(req.capacidadMax())
                .estado(req.estado() != null ? req.estado() : "ACTIVO")
                .build();
        return EstanqueMapper.toResponse(repo.save(e));
    }

    @Transactional
    public EstanqueResponse actualizar(Long id, EstanqueRequest req) {
        Estanque e = buscar(id);
        e.setNombre(req.nombre());
        e.setTipo(req.tipo());
        e.setAreaM2(req.areaM2());
        e.setVolumenM3(req.volumenM3());
        e.setCapacidadMax(req.capacidadMax());
        if (req.estado() != null) e.setEstado(req.estado());
        return EstanqueMapper.toResponse(repo.save(e));
    }

    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private Estanque buscar(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estanque", id));
    }
}
