package com.ecommerce.francoraspo.models.requests;

import com.ecommerce.francoraspo.models.entities.ProductoItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class CarritoRequest {
    private List<ProductoItem> productoItems;
    private String direccionEntrega;

}
