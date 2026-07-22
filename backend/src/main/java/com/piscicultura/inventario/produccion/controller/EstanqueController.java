package com.piscicultura.inventario.produccion.controller;

import com.piscicultura.inventario.produccion.dto.EstanqueRequest;
import com.piscicultura.inventario.produccion.dto.EstanqueResponse;
import com.piscicultura.inventario.produccion.service.EstanqueService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produccion/estanques")
public class EstanqueController {

    private final EstanqueService service;

    public EstanqueController(EstanqueService service) {
        this.service = service;
    }

    @GetMapping
    public List<EstanqueResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public EstanqueResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    public ResponseEntity<EstanqueResponse> crear(@Valid @RequestBody EstanqueRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PutMapping("/{id}")
    public EstanqueResponse actualizar(@PathVariable Long id, @Valid @RequestBody EstanqueRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
