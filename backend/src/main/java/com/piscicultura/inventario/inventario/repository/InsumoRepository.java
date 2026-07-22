package com.piscicultura.inventario.inventario.repository;

import com.piscicultura.inventario.inventario.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    Optional<Insumo> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<Insumo> findByActivoTrue();

    /** Insumos cuyo stock actual está en o por debajo del mínimo. */
    @Query("SELECT i FROM Insumo i WHERE i.stockActual <= i.stockMinimo AND i.activo = true")
    List<Insumo> findConStockBajo();
}
