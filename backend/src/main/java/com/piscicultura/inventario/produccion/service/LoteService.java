package com.piscicultura.inventario.produccion.service;

import com.piscicultura.inventario.common.RecursoNoEncontradoException;
import com.piscicultura.inventario.common.ReglaNegocioException;
import com.piscicultura.inventario.produccion.dto.LoteRequest;
import com.piscicultura.inventario.produccion.dto.LoteResponse;
import com.piscicultura.inventario.produccion.entity.Especie;
import com.piscicultura.inventario.produccion.entity.Estanque;
import com.piscicultura.inventario.produccion.entity.Lote;
import com.piscicultura.inventario.produccion.mapper.LoteMapper;
import com.piscicultura.inventario.produccion.repository.EspecieRepository;
import com.piscicultura.inventario.produccion.repository.EstanqueRepository;
import com.piscicultura.inventario.produccion.repository.LoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LoteService {

    private final LoteRepository loteRepo;
    private final EspecieRepository especieRepo;
    private final EstanqueRepository estanqueRepo;

    public LoteService(LoteRepository loteRepo, EspecieRepository especieRepo,
                       EstanqueRepository estanqueRepo) {
        this.loteRepo = loteRepo;
        this.especieRepo = especieRepo;
        this.estanqueRepo = estanqueRepo;
    }

    @Transactional(readOnly = true)
    public List<LoteResponse> listar() {
        return loteRepo.findAll().stream().map(LoteMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LoteResponse obtener(Long id) {
        return LoteMapper.toResponse(buscar(id));
    }

    @Transactional
    public LoteResponse crear(LoteRequest req) {
        if (loteRepo.existsByCodigo(req.codigo())) {
            throw new ReglaNegocioException("Ya existe un lote con el código " + req.codigo());
        }
        Lote l = Lote.builder()
                .codigo(req.codigo())
                .especie(especie(req.especieId()))
                .estanque(estanque(req.estanqueId()))
                .fechaSiembra(req.fechaSiembra())
                .cantidadInicial(req.cantidadInicial())
                .cantidadActual(req.cantidadActual() != null ? req.cantidadActual() : req.cantidadInicial())
                .pesoPromedioG(req.pesoPromedioG() != null ? req.pesoPromedioG() : BigDecimal.ZERO)
                .estado(req.estado() != null ? req.estado() : "ACTIVO")
                .observaciones(req.observaciones())
                .build();
        return LoteMapper.toResponse(loteRepo.save(l));
    }

    @Transactional
    public LoteResponse actualizar(Long id, LoteRequest req) {
        Lote l = buscar(id);
        l.setEspecie(especie(req.especieId()));
        l.setEstanque(estanque(req.estanqueId()));
        l.setFechaSiembra(req.fechaSiembra());
        l.setCantidadInicial(req.cantidadInicial());
        if (req.cantidadActual() != null) l.setCantidadActual(req.cantidadActual());
        if (req.pesoPromedioG() != null) l.setPesoPromedioG(req.pesoPromedioG());
        if (req.estado() != null) l.setEstado(req.estado());
        l.setObservaciones(req.observaciones());
        return LoteMapper.toResponse(loteRepo.save(l));
    }

    @Transactional
    public void eliminar(Long id) {
        loteRepo.delete(buscar(id));
    }

    private Lote buscar(Long id) {
        return loteRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lote", id));
    }

    private Especie especie(Long id) {
        return especieRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Especie", id));
    }

    private Estanque estanque(Long id) {
        return estanqueRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estanque", id));
    }
}
