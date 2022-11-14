package com.ecommerce.francoraspo.models.entities;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document(collection = "productos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Producto {
    @Id
    private String   id;
    @Column(name="codigo", nullable = false, length = 50)
    private String codigo;
    @Column(name="descripcion", nullable = false, length = 100)
    private String descripcion;
    private Double precioUnitario;
    @Column(name="categoria", nullable = false, length = 100)
    private String categoria;
}
