package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;

import java.util.Optional;

public interface UsuarioService {
        Optional<Usuario> obtenerUsuarioById(final String id);
        Optional<Usuario> eliminarUsuarioById(final String id);
        Usuario nuevoUsuario(final UsuarioRequest usuarioRequest);
        Optional<Usuario> actalizarUsuario(final String id, final UsuarioRequest usuarioRequest);
}
