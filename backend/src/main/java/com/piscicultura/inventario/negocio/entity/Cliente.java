package com.piscicultura.inventario.negocio.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cliente", schema = "negocio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "tipo_documento", nullable = false, length = 10)
    @Builder.Default
    private String tipoDocumento = "CC";

    @Column(length = 30, unique = true)
    private String documento;

    @Column(length = 30)
    private String telefono;

    @Column(length = 120)
    private String email;

    @Column(length = 200)
    private String direccion;

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
