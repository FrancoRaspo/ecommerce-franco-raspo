package com.ecommerce.francoraspo.models.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private String telefono;
    @NotBlank
    private String nombreUsuario;
    @NotBlank
    private String clave;
    @NotBlank
    private String email;
    private Set<String> roles;
}
