package com.piscicultura.inventario.inventario.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_insumo", schema = "inventario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CategoriaInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80, unique = true)
    private String nombre;

    @Column(length = 200)
    private String descripcion;
}
