package com.ecommerce.francoraspo.models.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="usuario", nullable = false, length = 100, unique=true)
    private String usuario;
    @Column(name="nombre", nullable = false, length = 100)
    private String nombre;
    @Column(name="telefono", nullable = false, length = 100)
    private String telefono;
    @Column(name="password", nullable = false, length = 100)
    private String password;
    @Column(name="email", nullable = false)
    private String email;
}
