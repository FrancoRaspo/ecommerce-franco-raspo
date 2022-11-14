package com.ecommerce.francoraspo.models.entities;


import lombok.*;

import java.util.Objects;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ProductoItem {
    private Producto producto;
    private long cantidad;

    public void actualizaCantidad(Long cantidad) {
        this.cantidad += cantidad;

        if (this.cantidad < 0 ) {
            this.cantidad = 0;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductoItem)) return false;
        ProductoItem that = (ProductoItem) o;
        return Objects.equals(producto.getId(), that.producto.getId());
    }
}
