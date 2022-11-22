package com.ecommerce.francoraspo.models.entities;

import com.ecommerce.francoraspo.models.enums.EOrdenEstado;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document(collection="ordenes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Orden {

    @Id
    private String id;

    @Column(nullable = false)
    private Long numeroOrden;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    private List<ProductoItem> productoItems;
    
    @Column(nullable = false)
    private String usuario;

    private Carrito carrito;

    Double monto;

    private EOrdenEstado estado;

    public Double getMonto() {

        if (productoItems == null) {
            return 0d;
        }
        return productoItems.stream()
                .mapToDouble(item -> item.getProducto().getPrecioUnitario() * item.getCantidad() )
                .sum();

    }

    public ProductoItem actualizaItem(ProductoItem ordenItem) {
        if (productoItems == null) {
            productoItems = new ArrayList<>();
        }

        if (!productoItems.contains(ordenItem)) {
            if (ordenItem.getCantidad() > 0 ) {
                productoItems.add(ordenItem);
            }
            else {
                ordenItem.setCantidad(0);
            }
            return ordenItem;
        } else {
            ProductoItem ordenItemActualizado = productoItems.get(productoItems.indexOf(ordenItem));
            ordenItemActualizado.actualizaCantidad(ordenItem.getCantidad());
            if (ordenItemActualizado.getCantidad() <= 0) {
                productoItems.remove(ordenItem);
                ordenItemActualizado.setCantidad(0);
            }
            return ordenItemActualizado;
        }

    }
}
