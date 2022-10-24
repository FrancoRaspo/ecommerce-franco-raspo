package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;

import java.util.Optional;

public interface UsuarioService {
        Optional<Usuario> getUsuarioById(final Long id);
        Optional<Usuario> deleteUsuarioById(final Long id);
        Usuario newUsuario(final UsuarioRequest usuarioRequest);
        Optional<Usuario>  updateUsuario(final Long id, final UsuarioRequest usuarioRequest);
}
