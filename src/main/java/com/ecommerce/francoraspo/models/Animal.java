package com.ecommerce.francoraspo.models;

public class Animal {
    private final String nombre;
    private final Integer edad;
    private final Boolean  esPadre;

    public String getNombre() {
        return nombre;
    }

    public Integer getEdad() {
        return edad;
    }

    public Boolean getEsPadre() {
        return esPadre;
    }

    public Animal(String nombre, Integer edad, Boolean  esPadre) {
        this.nombre = nombre;
        this.edad = edad;
        this.esPadre = esPadre;
    }
}
