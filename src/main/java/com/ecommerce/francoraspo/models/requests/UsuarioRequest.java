package com.ecommerce.francoraspo.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioRequest {
    @NotNull @NotBlank  @Size(min=4, max=20)
    private String usuario;
    @NotNull @NotBlank
    private String nombre;
    private String telefono;
    @NotNull @NotBlank
    private String password;
    @Email @NotNull @NotBlank
    private String email;
}
