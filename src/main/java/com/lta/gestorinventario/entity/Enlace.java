package com.lta.gestorinventario.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "enlaces")
@Data
public class Enlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String aplicacion;
    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}

