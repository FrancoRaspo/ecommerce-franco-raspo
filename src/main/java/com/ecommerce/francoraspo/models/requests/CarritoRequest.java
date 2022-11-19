package com.ecommerce.francoraspo.models.requests;

import com.ecommerce.francoraspo.models.entities.ProductoItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class CarritoRequest {
    private List<ProductoItem> productoItems;
    private String direccionEntrega;

}
