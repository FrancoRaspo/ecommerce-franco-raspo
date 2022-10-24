package com.ecommerce.francoraspo.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioRequest {
    private String usuario;
    private String nombre;
    private String telefono;
    private String password;
    private String email;

}
