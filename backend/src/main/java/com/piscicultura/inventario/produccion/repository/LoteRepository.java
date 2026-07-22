package com.piscicultura.inventario.produccion.repository;

import com.piscicultura.inventario.produccion.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    boolean existsByCodigo(String codigo);
    List<Lote> findByEstado(String estado);
    long countByEstado(String estado);
}
