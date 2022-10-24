package com.ecommerce.francoraspo.models.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="producto_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CarritoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    @Column(nullable = false)
    private Long cantidad;
}
