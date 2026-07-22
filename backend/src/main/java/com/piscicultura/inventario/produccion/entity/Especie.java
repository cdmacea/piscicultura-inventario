package com.piscicultura.inventario.produccion.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especie", schema = "produccion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Especie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_comun", nullable = false, length = 100, unique = true)
    private String nombreComun;

    @Column(name = "nombre_cientifico", length = 120)
    private String nombreCientifico;

    @Column(length = 300)
    private String descripcion;
}
