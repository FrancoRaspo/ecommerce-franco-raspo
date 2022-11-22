package com.ecommerce.francoraspo.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum ERolSeguridad implements GrantedAuthority {
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

    @Override
    public String getAuthority() {
        return this.name();
    }
}

