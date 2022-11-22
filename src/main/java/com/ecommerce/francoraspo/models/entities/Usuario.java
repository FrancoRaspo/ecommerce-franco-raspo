package com.ecommerce.francoraspo.models.entities;


import com.ecommerce.francoraspo.models.authJwtModels.RolSeguridad;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "usuarios")
@Setter
@Getter
@NoArgsConstructor

public class Usuario {
    @Id
    private String id;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 20)
    private String usuarioNombre;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @JsonIgnore
    @NotBlank
    @Size(max = 120)
    private String clave;
    @Column(name = "telefono", nullable = false, length = 100)
    private String telefono;
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    public Usuario(String usuarioNombre, String email, String clave, String telefono, String nombre) {
        this.usuarioNombre = usuarioNombre;
        this.email = email;
        this.clave = clave;
        this.telefono = telefono;
        this.nombre = nombre;
    }

    @DBRef
    private Set<RolSeguridad> roles = new HashSet<>();

    public Set<RolSeguridad> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolSeguridad> roles) {
        this.roles = roles;
    }


}
