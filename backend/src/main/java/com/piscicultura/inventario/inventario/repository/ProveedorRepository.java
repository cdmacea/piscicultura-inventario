package com.piscicultura.inventario.inventario.repository;

import com.piscicultura.inventario.inventario.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}
