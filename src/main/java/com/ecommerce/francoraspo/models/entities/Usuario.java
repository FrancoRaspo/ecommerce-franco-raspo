package com.ecommerce.francoraspo.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;

@Document(collection = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Usuario {
    @Id
    private String id;
    @Column(name="usuario", nullable = false, length = 100, unique=true)
    private String usuario;
    @Column(name="nombre", nullable = false, length = 100)
    private String nombre;
    @Column(name="telefono", nullable = false, length = 100)
    private String telefono;
    @JsonIgnore
    @Column(name="password", nullable = false, length = 100)
    private String password;
    @Column(name="email", nullable = false)
    private String email;
}

