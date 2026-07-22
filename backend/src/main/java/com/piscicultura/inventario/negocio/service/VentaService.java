package com.piscicultura.inventario.negocio.service;

import com.piscicultura.inventario.common.RecursoNoEncontradoException;
import com.piscicultura.inventario.common.ReglaNegocioException;
import com.piscicultura.inventario.negocio.dto.VentaRequest;
import com.piscicultura.inventario.negocio.dto.VentaResponse;
import com.piscicultura.inventario.negocio.entity.Cliente;
import com.piscicultura.inventario.negocio.entity.DetalleVenta;
import com.piscicultura.inventario.negocio.entity.EstadoVenta;
import com.piscicultura.inventario.negocio.entity.Venta;
import com.piscicultura.inventario.negocio.mapper.VentaMapper;
import com.piscicultura.inventario.negocio.repository.ClienteRepository;
import com.piscicultura.inventario.negocio.repository.VentaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class VentaService {

    private final VentaRepository ventaRepo;
    private final ClienteRepository clienteRepo;

    public VentaService(VentaRepository ventaRepo, ClienteRepository clienteRepo) {
        this.ventaRepo = ventaRepo;
        this.clienteRepo = clienteRepo;
    }

    @Transactional(readOnly = true)
    public Page<VentaResponse> listar(Pageable pageable) {
        return ventaRepo.findAll(pageable).map(VentaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public VentaResponse obtener(Long id) {
        return VentaMapper.toResponse(buscar(id));
    }

    @Transactional
    public VentaResponse crear(VentaRequest req) {
        if (ventaRepo.existsByCodigo(req.codigo())) {
            throw new ReglaNegocioException("Ya existe una venta con el código " + req.codigo());
        }
        Cliente cliente = clienteRepo.findById(req.clienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", req.clienteId()));

        Venta venta = Venta.builder()
                .codigo(req.codigo())
                .cliente(cliente)
                .fecha(req.fecha() != null ? req.fecha() : LocalDate.now())
                .metodoPago(req.metodoPago() != null ? req.metodoPago() : "EFECTIVO")
                .estado(EstadoVenta.PENDIENTE)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (var d : req.detalles()) {
            BigDecimal subtotal = d.cantidadKg().multiply(d.precioKg());
            DetalleVenta detalle = DetalleVenta.builder()
                    .loteId(d.loteId())
                    .descripcion(d.descripcion())
                    .cantidadKg(d.cantidadKg())
                    .precioKg(d.precioKg())
                    .subtotal(subtotal)
                    .build();
            venta.addDetalle(detalle);
            total = total.add(subtotal);
        }
        venta.setTotal(total);
        return VentaMapper.toResponse(ventaRepo.save(venta));
    }

    @Transactional
    public VentaResponse cambiarEstado(Long id, EstadoVenta estado) {
        Venta v = buscar(id);
        v.setEstado(estado);
        return VentaMapper.toResponse(ventaRepo.save(v));
    }

    private Venta buscar(Long id) {
        return ventaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta", id));
    }
}
