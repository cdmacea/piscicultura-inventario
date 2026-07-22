package com.piscicultura.inventario.inventario.controller;

import com.piscicultura.inventario.inventario.entity.CategoriaInsumo;
import com.piscicultura.inventario.inventario.entity.Proveedor;
import com.piscicultura.inventario.inventario.repository.CategoriaInsumoRepository;
import com.piscicultura.inventario.inventario.repository.ProveedorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Datos de referencia para poblar los formularios del inventario. */
@RestController
@RequestMapping("/inventario")
public class ReferenciaInventarioController {

    private final CategoriaInsumoRepository categoriaRepo;
    private final ProveedorRepository proveedorRepo;

    public ReferenciaInventarioController(CategoriaInsumoRepository categoriaRepo,
                                          ProveedorRepository proveedorRepo) {
        this.categoriaRepo = categoriaRepo;
        this.proveedorRepo = proveedorRepo;
    }

    @GetMapping("/categorias")
    public List<CategoriaInsumo> categorias() {
        return categoriaRepo.findAll();
    }

    @GetMapping("/proveedores")
    public List<Proveedor> proveedores() {
        return proveedorRepo.findAll();
    }
}
