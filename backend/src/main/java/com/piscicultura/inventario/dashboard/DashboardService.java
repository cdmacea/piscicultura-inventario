package com.piscicultura.inventario.dashboard;

import com.piscicultura.inventario.inventario.repository.InsumoRepository;
import com.piscicultura.inventario.negocio.entity.EstadoVenta;
import com.piscicultura.inventario.negocio.repository.VentaRepository;
import com.piscicultura.inventario.produccion.repository.LoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final InsumoRepository insumoRepo;
    private final LoteRepository loteRepo;
    private final VentaRepository ventaRepo;

    public DashboardService(InsumoRepository insumoRepo, LoteRepository loteRepo,
                            VentaRepository ventaRepo) {
        this.insumoRepo = insumoRepo;
        this.loteRepo = loteRepo;
        this.ventaRepo = ventaRepo;
    }

    @Transactional(readOnly = true)
    public ResumenResponse resumen() {
        long totalInsumos = insumoRepo.findByActivoTrue().size();
        long stockBajo = insumoRepo.findConStockBajo().size();
        long lotesActivos = loteRepo.countByEstado("ACTIVO");
        long peces = loteRepo.findByEstado("ACTIVO").stream()
                .mapToLong(l -> l.getCantidadActual() != null ? l.getCantidadActual() : 0)
                .sum();
        long ventasPendientes = ventaRepo.findByEstado(EstadoVenta.PENDIENTE).size();
        return new ResumenResponse(
                totalInsumos, stockBajo, lotesActivos, peces,
                ventaRepo.totalVendidoPagado(), ventasPendientes);
    }
}
