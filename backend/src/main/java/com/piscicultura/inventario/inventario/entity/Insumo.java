package com.piscicultura.inventario.inventario.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "insumo", schema = "inventario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id")
    private CategoriaInsumo categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(name = "unidad_medida", nullable = false, length = 20)
    @Builder.Default
    private String unidadMedida = "kg";

    @Column(name = "stock_actual", nullable = false, precision = 14, scale = 3)
    @Builder.Default
    private BigDecimal stockActual = BigDecimal.ZERO;

    @Column(name = "stock_minimo", nullable = false, precision = 14, scale = 3)
    @Builder.Default
    private BigDecimal stockMinimo = BigDecimal.ZERO;

    @Column(name = "precio_unitario", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal precioUnitario = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
