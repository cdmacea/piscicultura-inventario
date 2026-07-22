package com.piscicultura.inventario.produccion.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote", schema = "produccion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Lote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "especie_id")
    private Especie especie;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "estanque_id")
    private Estanque estanque;

    @Column(name = "fecha_siembra", nullable = false)
    private LocalDate fechaSiembra;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;

    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual;

    @Column(name = "peso_promedio_g", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal pesoPromedioG = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String estado = "ACTIVO";

    @Column(length = 300)
    private String observaciones;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (cantidadActual == null) cantidadActual = cantidadInicial;
    }
}
