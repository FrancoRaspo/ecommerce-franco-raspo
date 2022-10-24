package com.ecommerce.francoraspo.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="ordenes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Orden {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @Column(nullable = false)
    private Date fechaHora;

    @ManyToMany
    @JoinColumn(name = "producto_item_id", nullable = false)
    private List<CarritoItem> productoItems;

    private String direccionEntrega;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
