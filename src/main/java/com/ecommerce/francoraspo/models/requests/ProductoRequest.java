package com.ecommerce.francoraspo.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductoRequest {
    private String nombre;
    private String familia;
    private String marca;

}
