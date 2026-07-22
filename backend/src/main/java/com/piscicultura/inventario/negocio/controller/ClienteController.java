package com.piscicultura.inventario.negocio.controller;

import com.piscicultura.inventario.negocio.dto.ClienteRequest;
import com.piscicultura.inventario.negocio.dto.ClienteResponse;
import com.piscicultura.inventario.negocio.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/negocio/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<ClienteResponse> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ClienteResponse obtener(@PathVariable Long id) { return service.obtener(id); }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(req));
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
