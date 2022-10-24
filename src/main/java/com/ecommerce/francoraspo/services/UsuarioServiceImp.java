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
    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> deleteUsuarioById(Long id) {
        Optional<Usuario> usuarioDoc = usuarioRepository.findById(id);
        if (usuarioDoc.isPresent()) {
            usuarioRepository.delete(usuarioDoc.get());
            return usuarioDoc;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Usuario newUsuario(UsuarioRequest usuarioRequest) {

        final Usuario usuario = new Usuario();

        usuario.setUsuario(usuarioRequest.getUsuario());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setPassword(usuarioRequest.getPassword());

        usuarioRepository.save(usuario);

        return usuario;
    }

    @Override
    public Optional<Usuario> updateUsuario(Long id, UsuarioRequest usuarioRequest) {

        final Optional<Usuario> usuarioDoc = usuarioRepository.findById(id);

        if (usuarioDoc.isPresent()) {
            final Usuario usuario = usuarioDoc.get();

            usuario.setUsuario(usuarioRequest.getUsuario());
            usuario.setEmail(usuarioRequest.getEmail());
            usuario.setPassword(usuarioRequest.getPassword());

            usuarioRepository.save(usuario);
            return Optional.of(usuario);
        } else {
             return Optional.empty();
        }

    }
}
