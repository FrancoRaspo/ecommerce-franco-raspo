package com.ecommerce.francoraspo.models.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="descripcion", nullable = false, length = 100)
    private String descripcion;
    private Double precioUnitario;
    @Column(name="categoria", nullable = false, length = 100)
    private String categoria;
}
