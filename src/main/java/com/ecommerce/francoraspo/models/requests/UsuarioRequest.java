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
public class UsuarioRequest {
    private String nombre;
    private String telefono;
    @NotNull @NotBlank
    private String nombreUsuario;
    private String clave;
    private String email;
    private Set<String> roles;
}
