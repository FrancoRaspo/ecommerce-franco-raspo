package com.ecommerce.francoraspo.models.enums;

public enum EOrdenEstado {
    GENERADA(0,"Abierta"),
    VENDIDA(1,"Cerrada");

    private final int codigoEstado;
    private final String descripcion;

    EOrdenEstado(int codigoEstado, String descripcion) {
        this.descripcion=descripcion;
        this.codigoEstado=codigoEstado;
    }
    public int codigoEstado() {
        return this.codigoEstado;
    }
    public String toString() {
        return this.codigoEstado + " " + this.descripcion;
    }
}
