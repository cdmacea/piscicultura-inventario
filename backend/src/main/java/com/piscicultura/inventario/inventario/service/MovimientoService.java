package com.piscicultura.inventario.inventario.service;

import com.piscicultura.inventario.common.RecursoNoEncontradoException;
import com.piscicultura.inventario.common.ReglaNegocioException;
import com.piscicultura.inventario.inventario.dto.MovimientoRequest;
import com.piscicultura.inventario.inventario.dto.MovimientoResponse;
import com.piscicultura.inventario.inventario.entity.Insumo;
import com.piscicultura.inventario.inventario.entity.MovimientoInventario;
import com.piscicultura.inventario.inventario.entity.TipoMovimiento;
import com.piscicultura.inventario.inventario.mapper.MovimientoMapper;
import com.piscicultura.inventario.inventario.repository.InsumoRepository;
import com.piscicultura.inventario.inventario.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Registra entradas, salidas y ajustes, y mantiene actualizado el stock del insumo.
 */
@Service
public class MovimientoService {

    private final MovimientoInventarioRepository movimientoRepo;
    private final InsumoRepository insumoRepo;

    public MovimientoService(MovimientoInventarioRepository movimientoRepo,
                             InsumoRepository insumoRepo) {
        this.movimientoRepo = movimientoRepo;
        this.insumoRepo = insumoRepo;
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarRecientes() {
        return movimientoRepo.findTop50ByOrderByFechaDesc()
                .stream().map(MovimientoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarPorInsumo(Long insumoId) {
        return movimientoRepo.findByInsumoIdOrderByFechaDesc(insumoId)
                .stream().map(MovimientoMapper::toResponse).toList();
    }

    @Transactional
    public MovimientoResponse registrar(MovimientoRequest req) {
        Insumo insumo = insumoRepo.findById(req.insumoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Insumo", req.insumoId()));

        BigDecimal nuevoStock = calcularNuevoStock(insumo.getStockActual(), req.tipo(), req.cantidad());
        if (nuevoStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException(
                    "Stock insuficiente de '" + insumo.getNombre() + "'. Disponible: " + insumo.getStockActual());
        }

        insumo.setStockActual(nuevoStock);
        insumoRepo.save(insumo);

        MovimientoInventario mov = MovimientoInventario.builder()
                .insumo(insumo)
                .tipo(req.tipo())
                .cantidad(req.cantidad())
                .stockResultante(nuevoStock)
                .motivo(req.motivo())
                .usuario(req.usuario())
                .build();

        return MovimientoMapper.toResponse(movimientoRepo.save(mov));
    }

    private BigDecimal calcularNuevoStock(BigDecimal actual, TipoMovimiento tipo, BigDecimal cantidad) {
        return switch (tipo) {
            case ENTRADA -> actual.add(cantidad);
            case SALIDA  -> actual.subtract(cantidad);
            case AJUSTE  -> cantidad; // el ajuste fija el stock al valor indicado
        };
    }
}
