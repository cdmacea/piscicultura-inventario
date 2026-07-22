package com.piscicultura.inventario.produccion.controller;

import com.piscicultura.inventario.produccion.dto.LoteRequest;
import com.piscicultura.inventario.produccion.dto.LoteResponse;
import com.piscicultura.inventario.produccion.service.LoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produccion/lotes")
public class LoteController {

    private final LoteService service;

    public LoteController(LoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoteResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public LoteResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    public ResponseEntity<LoteResponse> crear(@Valid @RequestBody LoteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PutMapping("/{id}")
    public LoteResponse actualizar(@PathVariable Long id, @Valid @RequestBody LoteRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
