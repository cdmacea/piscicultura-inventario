package com.piscicultura.inventario.negocio.repository;

import com.piscicultura.inventario.negocio.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByDocumento(String documento);
}
