package com.ecommerce.francoraspo.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductoRequest {
    @NotNull @NotBlank   @Size(min=4, max=50)
    private String codigo;
    @NotNull @NotBlank
    private String descripcion;
    @NotNull @NumberFormat
    private Double precioUnitario;
    @NotNull @NotBlank @Size(min=4, max=100)
    private String categoria;
}
