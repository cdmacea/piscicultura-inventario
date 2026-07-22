package com.piscicultura.inventario.negocio.controller;

import com.piscicultura.inventario.negocio.dto.VentaRequest;
import com.piscicultura.inventario.negocio.dto.VentaResponse;
import com.piscicultura.inventario.negocio.entity.EstadoVenta;
import com.piscicultura.inventario.negocio.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/negocio/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @GetMapping
    public Page<VentaResponse> listar(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100), Sort.by(Sort.Direction.DESC, "fecha"));
        return service.listar(pageable);
    }

    @GetMapping("/{id}")
    public VentaResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    public ResponseEntity<VentaResponse> crear(@Valid @RequestBody VentaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PatchMapping("/{id}/estado")
    public VentaResponse cambiarEstado(@PathVariable Long id, @RequestParam EstadoVenta estado) {
        return service.cambiarEstado(id, estado);
    }
}
