package com.piscicultura.inventario.produccion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estanque", schema = "produccion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Estanque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String codigo;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String tipo = "TIERRA";

    @Column(name = "area_m2", precision = 12, scale = 2)
    private BigDecimal areaM2;

    @Column(name = "volumen_m3", precision = 12, scale = 2)
    private BigDecimal volumenM3;

    @Column(name = "capacidad_max")
    private Integer capacidadMax;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "ACTIVO";

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
