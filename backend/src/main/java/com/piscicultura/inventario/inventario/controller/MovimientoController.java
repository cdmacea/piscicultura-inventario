package com.piscicultura.inventario.inventario.controller;

import com.piscicultura.inventario.inventario.dto.MovimientoRequest;
import com.piscicultura.inventario.inventario.dto.MovimientoResponse;
import com.piscicultura.inventario.inventario.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventario/movimientos")
public class MovimientoController {

    private final MovimientoService service;

    public MovimientoController(MovimientoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovimientoResponse> recientes() {
        return service.listarRecientes();
    }

    @GetMapping("/insumo/{insumoId}")
    public List<MovimientoResponse> porInsumo(@PathVariable Long insumoId) {
        return service.listarPorInsumo(insumoId);
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid @RequestBody MovimientoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(req));
    }
}
