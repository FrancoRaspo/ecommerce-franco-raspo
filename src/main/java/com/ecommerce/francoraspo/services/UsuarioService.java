package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.handlers.exceptions.NoAuthorizedException;
import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.requests.UsuarioNewRequest;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;

import java.util.Optional;

public interface UsuarioService {

    Optional<Usuario> nuevoUsuario(UsuarioNewRequest usuarioRequest) throws Exception;

    Optional<Usuario> eliminarUsuario(String nombreUsuario) throws EntityNotFoundException, NoAuthorizedException;

    Optional<Usuario> actualizarUsuario(UsuarioRequest usuarioRequest) throws Exception;

}

