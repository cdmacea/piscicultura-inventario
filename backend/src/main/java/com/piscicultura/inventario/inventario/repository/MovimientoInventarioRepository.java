package com.piscicultura.inventario.inventario.repository;

import com.piscicultura.inventario.inventario.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByInsumoIdOrderByFechaDesc(Long insumoId);

    List<MovimientoInventario> findTop50ByOrderByFechaDesc();
}
