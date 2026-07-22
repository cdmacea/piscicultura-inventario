package com.piscicultura.inventario.produccion.controller;

import com.piscicultura.inventario.produccion.entity.Especie;
import com.piscicultura.inventario.produccion.repository.EspecieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produccion")
public class ReferenciaProduccionController {

    private final EspecieRepository especieRepo;

    public ReferenciaProduccionController(EspecieRepository especieRepo) {
        this.especieRepo = especieRepo;
    }

    @GetMapping("/especies")
    public List<Especie> especies() {
        return especieRepo.findAll();
    }
}
