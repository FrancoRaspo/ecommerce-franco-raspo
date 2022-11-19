package com.ecommerce.francoraspo.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private Instant fechaHora;
    private T detalle;
    private int codigoRepuesta;
    private String estatus;
}
