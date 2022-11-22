package com.ecommerce.francoraspo.models.entities;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carritos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Carrito {

    @Id
    private String id;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    private List<ProductoItem> productoItems;

    private String direccionEntrega;

    private String usuarioNombre;

    public ProductoItem actualizaItem(ProductoItem carritoItem) {
        if (productoItems == null) {
            productoItems = new ArrayList<>();
        }

        if (!productoItems.contains(carritoItem)) {
            if (carritoItem.getCantidad() > 0) {
                productoItems.add(carritoItem);
            } else {
                carritoItem.setCantidad(0);
            }
            return carritoItem;
        } else {
            ProductoItem carritoItemActualizado = productoItems.get(productoItems.indexOf(carritoItem));
            carritoItemActualizado.actualizaCantidad(carritoItem.getCantidad());
            if (carritoItemActualizado.getCantidad() <= 0) {
                productoItems.remove(carritoItem);
                carritoItemActualizado.setCantidad(0);
            }
            return carritoItemActualizado;
        }

    }


}
