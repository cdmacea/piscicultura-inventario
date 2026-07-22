package com.piscicultura.inventario.negocio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta", schema = "negocio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @Column(name = "lote_id")
    private Long loteId;

    @Column(nullable = false, length = 150)
    private String descripcion;

    @Column(name = "cantidad_kg", nullable = false, precision = 12, scale = 3)
    private BigDecimal cantidadKg;

    @Column(name = "precio_kg", nullable = false, precision = 14, scale = 2)
    private BigDecimal precioKg;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal subtotal;
}
