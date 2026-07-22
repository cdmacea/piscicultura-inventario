package com.piscicultura.inventario.negocio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta", schema = "negocio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoVenta estado = EstadoVenta.PENDIENTE;

    @Column(name = "metodo_pago", length = 20)
    @Builder.Default
    private String metodoPago = "EFECTIVO";

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DetalleVenta> detalles = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (fecha == null) fecha = LocalDate.now();
    }

    public void addDetalle(DetalleVenta d) {
        d.setVenta(this);
        detalles.add(d);
    }
}
