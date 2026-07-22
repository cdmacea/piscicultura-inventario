package com.piscicultura.inventario.produccion.repository;

import com.piscicultura.inventario.produccion.entity.Estanque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstanqueRepository extends JpaRepository<Estanque, Long> {
    Optional<Estanque> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
