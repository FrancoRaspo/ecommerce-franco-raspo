package com.ecommerce.francoraspo.models.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name="carrito")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private Date fechaHora;

    @ManyToMany
    @JoinColumn(name = "producto_item_id", nullable = false)
    private List<CarritoItem> productoItems;

    private String direccionEntrega;




}
