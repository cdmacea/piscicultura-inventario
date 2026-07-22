package com.piscicultura.inventario.produccion.repository;

import com.piscicultura.inventario.produccion.entity.Especie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecieRepository extends JpaRepository<Especie, Long> {
}
