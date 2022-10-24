package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;
import com.ecommerce.francoraspo.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;


    @GetMapping(value = "usuario/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getUsuarioById(@PathVariable(name="id") final Long id) {
        final Optional<Usuario> usuario = usuarioService.getUsuarioById(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "usuario/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteUsuarioById(@PathVariable(name="id") final Long id) {
        final Optional<Usuario> usuario = usuarioService.deleteUsuarioById(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "usuario", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newUsuario(@RequestBody UsuarioRequest usuarioRequest){

        try {
            final Usuario usuario = usuarioService.newUsuario(usuarioRequest);
            return ResponseEntity.created(URI.create("")).body(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @PutMapping(value = "usuario/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUsuario(@PathVariable(name = "id") final Long id,
                                               @RequestBody final UsuarioRequest usuarioRequest) {

        final Optional<Usuario> usuario = usuarioService.getUsuarioById(id);

        if (usuario.isPresent()) {
            try {
                final Optional<Usuario> usuarioGuardado = usuarioService.updateUsuario(id, usuarioRequest);
                if (usuarioGuardado.isPresent()) {
                    return ResponseEntity.ok(usuarioGuardado.get());
                }
                else
                {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
