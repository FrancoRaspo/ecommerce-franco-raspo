package com.ecommerce.francoraspo.controllers;


import com.ecommerce.francoraspo.config.security.JwtUtils;
import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.models.authJwtModels.RolSeguridad;
import com.ecommerce.francoraspo.models.authJwtModels.UserDetailsImpl;
import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.enums.ERolSeguridad;
import com.ecommerce.francoraspo.models.requests.LoginRequest;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;
import com.ecommerce.francoraspo.models.responses.JwtResponse;
import com.ecommerce.francoraspo.repositories.RoleRepository;
import com.ecommerce.francoraspo.repositories.UsuarioRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UsuarioController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UsuarioRepository userNewRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    Logger logger = LogManager.getLogger(UsuarioController.class);

    @PostMapping("/signin")
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

    @PostMapping("/usuario")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UsuarioRequest signUpRequest) throws ApiRestException {

        logger.info("POST /api/auth/usuario");
        logger.debug(signUpRequest.toString());

        if (userNewRepository.existsByUsuarioNombre(signUpRequest.getNombreUsuario())) {
            throw new ApiRestException("Ya existe un usuario con ese nombre");
        }

        if (userNewRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new ApiRestException("Ya hay registrado un usuario con ese e-mail");
        }

        // Create new user's account
        Usuario user = new Usuario(signUpRequest.getNombreUsuario(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getClave()),
                signUpRequest.getTelefono(),
                signUpRequest.getNombre()

                );

        Set<String> strRoles = signUpRequest.getRoles();
        Set<RolSeguridad> roles = new HashSet<>();

        if (strRoles == null) {
            RolSeguridad userRole = roleRepository.findByName(ERolSeguridad.USUARIO)
                    .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.USUARIO + " no existe."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "administrador":
                        RolSeguridad adminRole = roleRepository.findByName(ERolSeguridad.ADMINISTRADOR)
                                .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.ADMINISTRADOR + " no existe."));
                        roles.add(adminRole);

                        break;
                    case "invitado":
                        RolSeguridad modRole = roleRepository.findByName(ERolSeguridad.INVITADO)
                                .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.INVITADO + " no existe."));
                        roles.add(modRole);

                        break;
                    default:
                        RolSeguridad userRole = roleRepository.findByName(ERolSeguridad.USUARIO)
                                .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.USUARIO + " no existe."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userNewRepository.save(user);

        return new ResponseEntity<>("Usuario registrado con exito!", HttpStatus.OK);
    }

    @PatchMapping("/usuario")
    public ResponseEntity<?> actualizaUser(@Valid @RequestBody UsuarioRequest signUpRequest) throws ApiRestException, EntityNotFoundException {

        logger.info("PATCH /api/auth/usuario");

        Optional<Usuario> user =
                userNewRepository.findByUsuarioNombre(signUpRequest.getNombreUsuario());

        if(user.isEmpty()) {
            throw new EntityNotFoundException("No existe un usuario con ese nombre");
        }

        Usuario usuario = user.get();

        List<Usuario> userList =
                userNewRepository.findAllByEmail(signUpRequest.getEmail()).stream()
                        .filter(u -> !u.getUsuarioNombre().equals(signUpRequest.getNombreUsuario()) )
                        .collect(Collectors.toList());

        if (userList.size() != 0) {
            throw new ApiRestException("Ya hay registrado un usuario con ese e-mail");
        }



        Set<String> strRoles = signUpRequest.getRoles();
        Set<RolSeguridad> roles = new HashSet<>();

        if (strRoles == null) {
            RolSeguridad userRole = roleRepository.findByName(ERolSeguridad.USUARIO)
                    .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.USUARIO + " no existe."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "administrador":
                        RolSeguridad adminRole = roleRepository.findByName(ERolSeguridad.ADMINISTRADOR)
                                .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.ADMINISTRADOR + " no existe."));
                        roles.add(adminRole);

                        break;
                    case "invitado":
                        RolSeguridad modRole = roleRepository.findByName(ERolSeguridad.INVITADO)
                                .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.INVITADO + " no existe."));
                        roles.add(modRole);

                        break;
                    default:
                        RolSeguridad userRole = roleRepository.findByName(ERolSeguridad.USUARIO)
                                .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.USUARIO + " no existe."));
                        roles.add(userRole);
                }
            });
        }
        usuario.setRoles(roles);
        userNewRepository.save(usuario);

        return new ResponseEntity<>("Usuario actualizado con éxito!", HttpStatus.OK);
    }

    @DeleteMapping("/usuario/{nombreUsuario}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "nombreUsuario") String nombreUsuario) throws ApiRestException, EntityNotFoundException {

        logger.info("DELETE /api/auth/usuario/{nombreUsuario}");

        Optional<Usuario> user =
                userNewRepository.findByUsuarioNombre(nombreUsuario);

        if(user.isEmpty()) {
            throw new EntityNotFoundException("No existe un usuario con ese nombre");
        }

        Usuario usuario = user.get();

        userNewRepository.delete(usuario);

        return new ResponseEntity<>("Usuario eliminado con éxito!", HttpStatus.OK);
    }
}
