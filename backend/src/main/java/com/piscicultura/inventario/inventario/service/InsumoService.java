package com.piscicultura.inventario.inventario.service;

import com.piscicultura.inventario.common.RecursoNoEncontradoException;
import com.piscicultura.inventario.common.ReglaNegocioException;
import com.piscicultura.inventario.inventario.dto.InsumoRequest;
import com.piscicultura.inventario.inventario.dto.InsumoResponse;
import com.piscicultura.inventario.inventario.entity.CategoriaInsumo;
import com.piscicultura.inventario.inventario.entity.Insumo;
import com.piscicultura.inventario.inventario.entity.Proveedor;
import com.piscicultura.inventario.inventario.mapper.InsumoMapper;
import com.piscicultura.inventario.inventario.repository.CategoriaInsumoRepository;
import com.piscicultura.inventario.inventario.repository.InsumoRepository;
import com.piscicultura.inventario.inventario.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepo;
    private final CategoriaInsumoRepository categoriaRepo;
    private final ProveedorRepository proveedorRepo;

    public InsumoService(InsumoRepository insumoRepo,
                         CategoriaInsumoRepository categoriaRepo,
                         ProveedorRepository proveedorRepo) {
        this.insumoRepo = insumoRepo;
        this.categoriaRepo = categoriaRepo;
        this.proveedorRepo = proveedorRepo;
    }

    @Transactional(readOnly = true)
    public List<InsumoResponse> listar() {
        return insumoRepo.findAll().stream().map(InsumoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<InsumoResponse> listarStockBajo() {
        return insumoRepo.findConStockBajo().stream().map(InsumoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public InsumoResponse obtener(Long id) {
        return InsumoMapper.toResponse(buscar(id));
    }

    @Transactional
    public InsumoResponse crear(InsumoRequest req) {
        if (insumoRepo.existsByCodigo(req.codigo())) {
            throw new ReglaNegocioException("Ya existe un insumo con el código " + req.codigo());
        }
        Insumo insumo = Insumo.builder()
                .codigo(req.codigo())
                .nombre(req.nombre())
                .categoria(categoria(req.categoriaId()))
                .proveedor(proveedorOpcional(req.proveedorId()))
                .unidadMedida(req.unidadMedida())
                .stockActual(req.stockInicial() != null ? req.stockInicial() : BigDecimal.ZERO)
                .stockMinimo(req.stockMinimo())
                .precioUnitario(req.precioUnitario())
                .activo(req.activo() != null ? req.activo() : true)
                .build();
        return InsumoMapper.toResponse(insumoRepo.save(insumo));
    }

    @Transactional
    public InsumoResponse actualizar(Long id, InsumoRequest req) {
        Insumo insumo = buscar(id);
        insumo.setNombre(req.nombre());
        insumo.setCategoria(categoria(req.categoriaId()));
        insumo.setProveedor(proveedorOpcional(req.proveedorId()));
        insumo.setUnidadMedida(req.unidadMedida());
        insumo.setStockMinimo(req.stockMinimo());
        insumo.setPrecioUnitario(req.precioUnitario());
        if (req.activo() != null) insumo.setActivo(req.activo());
        return InsumoMapper.toResponse(insumoRepo.save(insumo));
    }

    @Transactional
    public void eliminar(Long id) {
        Insumo insumo = buscar(id);
        insumo.setActivo(false); // baja lógica: conserva el historial de movimientos
        insumoRepo.save(insumo);
    }

    private Insumo buscar(Long id) {
        return insumoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Insumo", id));
    }

    private CategoriaInsumo categoria(Long id) {
        return categoriaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }

    private Proveedor proveedorOpcional(Long id) {
        if (id == null) return null;
        return proveedorRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor", id));
    }
}
