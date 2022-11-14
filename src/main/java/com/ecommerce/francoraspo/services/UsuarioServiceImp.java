package com.ecommerce.francoraspo.services;


import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;
import com.ecommerce.francoraspo.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor

public class UsuarioServiceImp implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    @Override
    public Optional<Usuario> obtenerUsuarioById(String id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> eliminarUsuarioById(String id) {
        Optional<Usuario> usuarioDoc = usuarioRepository.findById(id);
        if (usuarioDoc.isPresent()) {
            usuarioRepository.delete(usuarioDoc.get());
            return usuarioDoc;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Usuario nuevoUsuario(UsuarioRequest usuarioRequest) {

        final Usuario usuario = new Usuario();

        usuario.setUsuario(usuarioRequest.getUsuario());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setPassword(usuarioRequest.getPassword());
        usuario.setNombre(usuarioRequest.getNombre());
        usuario.setTelefono(usuarioRequest.getTelefono());

        usuarioRepository.save(usuario);

        return usuario;
    }

    @Override
    public Optional<Usuario> actalizarUsuario(String id, UsuarioRequest usuarioRequest) {

        final Optional<Usuario> usuarioDoc = usuarioRepository.findById(id);

        if (usuarioDoc.isPresent()) {
            final Usuario usuario = usuarioDoc.get();

            usuario.setUsuario(usuarioRequest.getUsuario());
            usuario.setEmail(usuarioRequest.getEmail());
            usuario.setPassword(usuarioRequest.getPassword());
            usuario.setNombre(usuarioRequest.getNombre());
            usuario.setTelefono(usuarioRequest.getTelefono());

            usuarioRepository.save(usuario);
            return Optional.of(usuario);
        } else {
             return Optional.empty();
        }

    }
}
