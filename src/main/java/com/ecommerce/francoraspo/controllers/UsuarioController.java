package com.ecommerce.francoraspo.controllers;


import com.ecommerce.francoraspo.config.security.JwtUtils;
import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.handlers.exceptions.NoAuthorizedException;
import com.ecommerce.francoraspo.models.authJwtModels.UserDetailsImpl;
import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.requests.LoginRequest;
import com.ecommerce.francoraspo.models.requests.UsuarioNewRequest;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;
import com.ecommerce.francoraspo.models.responses.JwtResponse;
import com.ecommerce.francoraspo.models.responses.Response;
import com.ecommerce.francoraspo.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UsuarioController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    JwtUtils jwtUtils;

    Logger logger = LogManager.getLogger(UsuarioController.class);


    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws UnsupportedEncodingException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsuarioNombre(), loginRequest.getClave()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles
                )
        );
    }

    @PostMapping("/auth/usuario")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsuarioNewRequest usuarioRequest) throws ApiRestException, NoAuthorizedException {

        logger.info("POST /api/auth/usuario");

        try {
            Optional<Usuario> usuario = usuarioService.nuevoUsuario(usuarioRequest);

            if (usuario.isPresent()) {
                final Response<String> result = new Response(Instant.now(), "Usuario registrado con exito!" ,
                        HttpStatus.CREATED.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } else {
                throw new ApiRestException("Ocurrió un error al registrar el usuario.");
            }
        } catch (NoAuthorizedException e) {
            throw new NoAuthorizedException(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }
    }

    @PatchMapping("/usuario")
    public ResponseEntity<?> actualizaUser(@Valid @RequestBody UsuarioRequest usuarioRequest) throws ApiRestException, EntityNotFoundException {

        logger.info("PATCH /api/usuario");

        try {
            Optional<Usuario> usuario = usuarioService.actualizarUsuario(usuarioRequest);

            if (usuario.isPresent()) {
                final Response<String> result = new Response(Instant.now(), "Usuario actualizado con éxito!" ,
                        HttpStatus.OK.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                logger.error("Ocurrió un error al actualizar el usuario");
                throw new ApiRestException("Ocurrió un error al actualizar el usuario");
            }
        } catch (NoAuthorizedException nae) {
            logger.info("Acceso denegado!");
            final Response<String> result = new Response(Instant.now(), "Acceso denegado!" ,
                    HttpStatus.UNAUTHORIZED.value(), "Success");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException entityNotFoundException) {
            logger.error(entityNotFoundException.getMessage());
            throw entityNotFoundException;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }
    }

    @DeleteMapping("/usuario/{nombreUsuario}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "nombreUsuario") String nombreUsuario) throws ApiRestException, EntityNotFoundException {

        logger.info("DELETE /api/usuario/{nombreUsuario}");

        try {
            Optional<Usuario> usuario = usuarioService.eliminarUsuario(nombreUsuario);

            if (usuario.isPresent()) {
                final Response<String> result = new Response(Instant.now(), "Usuario eliminado con éxito!" ,
                        HttpStatus.OK.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                logger.error("Ocurrió un error al eliminar el usuario");
                throw new ApiRestException("Ocurrió un error al eliminar el usuario");
            }

        } catch (NoAuthorizedException nae) {
            logger.info("Acceso denegado!");
            final Response<String> result = new Response(Instant.now(), "Acceso denegado!" ,
                    HttpStatus.UNAUTHORIZED.value(), "Success");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException entityNotFoundException) {
            logger.error(entityNotFoundException.getMessage());
            throw entityNotFoundException;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }
    }
}
