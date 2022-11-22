package com.ecommerce.francoraspo.models.enums;

public enum ERolSeguridad {
    USUARIO("USUARIO"),
    INVITADO("INVITADO"),
    ADMINISTRADOR("ADMINISTRADOR");

    final String value;
    ERolSeguridad(String role) {
        this.value=role;
    }
    @Override
    public String toString() {
        return value;
    }
}

