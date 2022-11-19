package com.ecommerce.francoraspo.models.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequest {
    @NotBlank
    @NotNull
    private String usuarioNombre;
    @NotBlank
    @NotNull
    private String clave;
}
