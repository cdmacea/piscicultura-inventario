package com.piscicultura.inventario.inventario.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario", schema = "inventario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimiento tipo;

    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal cantidad;

    @Column(name = "stock_resultante", nullable = false, precision = 14, scale = 3)
    private BigDecimal stockResultante;

    @Column(length = 200)
    private String motivo;

    @Column(length = 80)
    private String usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @PrePersist
    void prePersist() {
        if (fecha == null) fecha = LocalDateTime.now();
    }
}
