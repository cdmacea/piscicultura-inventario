package com.piscicultura.inventario.inventario.controller;

import com.piscicultura.inventario.inventario.dto.InsumoRequest;
import com.piscicultura.inventario.inventario.dto.InsumoResponse;
import com.piscicultura.inventario.inventario.service.InsumoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario/insumos")
public class InsumoController {

    private final InsumoService service;

    public InsumoController(InsumoService service) {
        this.service = service;
    }

    @GetMapping
    public List<InsumoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/stock-bajo")
    public List<InsumoResponse> stockBajo() {
        return service.listarStockBajo();
    }

    @GetMapping("/{id}")
    public InsumoResponse obtener(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<InsumoResponse> crear(@Valid @RequestBody InsumoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PutMapping("/{id}")
    public InsumoResponse actualizar(@PathVariable Long id, @Valid @RequestBody InsumoRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
