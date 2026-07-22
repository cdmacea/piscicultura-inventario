package com.piscicultura.inventario.negocio.repository;

import com.piscicultura.inventario.negocio.entity.EstadoVenta;
import com.piscicultura.inventario.negocio.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    boolean existsByCodigo(String codigo);

    List<Venta> findByEstado(EstadoVenta estado);

    @Query("SELECT COALESCE(SUM(v.total),0) FROM Venta v WHERE v.estado = 'PAGADA'")
    BigDecimal totalVendidoPagado();
}
