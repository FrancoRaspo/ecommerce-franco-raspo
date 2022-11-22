package com.ecommerce.francoraspo.models.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioNewRequest {
    @NotNull @NotBlank
    private String nombre;
    @NotNull @NotBlank
    private String telefono;
    @NotNull @NotBlank
    private String nombreUsuario;
    @NotNull @NotBlank
    private String clave;
    @NotNull @NotBlank
    private String email;
    @NotNull
    private Set<String> roles;
}
