package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.handlers.exceptions.NoAuthorizedException;
import com.ecommerce.francoraspo.models.authJwtModels.RolSeguridad;
import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.models.enums.ERolSeguridad;
import com.ecommerce.francoraspo.models.requests.UsuarioNewRequest;
import com.ecommerce.francoraspo.models.requests.UsuarioRequest;
import com.ecommerce.francoraspo.repositories.RoleRepository;
import com.ecommerce.francoraspo.repositories.UsuarioRepository;
import com.ecommerce.francoraspo.services.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * <h1>Usuario Service</h1>
 * Esta clase administra los usuarios
 * Solo los usuarios con Rol de administrador pueden administrar usuarios con rol de administrador
 * Los usuario de la sesión solo pueden modificar sus datos
 * @author Franco Raspo
 * @version 1.0
 * @since 2022-11-21
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;
    /**
     * <h2>prepararRoles</h2>
     * Asigna los roles del usuario, por defecto se asigna el rol USUARIO
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    private Set<RolSeguridad> prepararRoles(Set<String> rolesUsuario) {
        Set<RolSeguridad> roles = new HashSet<>();

        if (rolesUsuario == null) {
            RolSeguridad userRole = roleRepository.findByName(ERolSeguridad.USUARIO)
                    .orElseThrow(() -> new RuntimeException("El rol " + ERolSeguridad.USUARIO + " no existe."));
            roles.add(userRole);
        } else {
            rolesUsuario.forEach(role -> {
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

        return roles;

    }

    /**
     * <h2>nuevoUsuario</h2>
     * Crea un nuevo usuario
     * Si la colección usuarios no tiene documento, se puede crear el primer usuario administrador
     * Solo usuarios administradores pueden crear otros usuario con ese rol
     * No se pueden repetir nombres de usuario, ni correos electrónicos
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    public Optional<Usuario> nuevoUsuario(UsuarioNewRequest usuarioRequest) throws Exception {

        boolean nuevoAdmin =
                (usuarioRequest.getRoles().stream().filter(r -> r.equals("administrador")).count()) > 0 ?
                        true : false;

        if (nuevoAdmin && usuarioRepository.count() > 0 && !UserDetailsService.IsAdmin()) {
            throw new NoAuthorizedException("Acceso denegado!");
        }

        if (usuarioRepository.existsByUsuarioNombre(usuarioRequest.getNombreUsuario())) {
            throw new Exception("Ya existe un usuario con ese nombre");
        }

        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new Exception("Ya hay registrado un usuario con ese e-mail");
        }

        // Create new usuario's account
        Usuario usuario = new Usuario(usuarioRequest.getNombreUsuario(),
                usuarioRequest.getEmail(),
                encoder.encode(usuarioRequest.getClave()),
                usuarioRequest.getTelefono(),
                usuarioRequest.getNombre()

        );
        usuario.setRoles(prepararRoles(usuarioRequest.getRoles()));
        usuarioRepository.save(usuario);

        return Optional.of(usuario);
    }

    /**
     * <h2>eliminarUsuario</h2>
     * Elimina un nuevo usuario
     * Solo usuarios administradores pueden eliminar usuarios varios
     * Solo un usuario puede eliminarse a si mismo
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    public Optional<Usuario> eliminarUsuario(String nombreUsuario) throws EntityNotFoundException, NoAuthorizedException {

        if (nombreUsuario == UserDetailsService.UsuarioContext() ||
                UserDetailsService.IsAdmin()) {

            Optional<Usuario> user =
                    usuarioRepository.findByUsuarioNombre(nombreUsuario);

            if (user.isEmpty()) {
                throw new EntityNotFoundException("No existe un usuario con ese nombre");
            }

            Usuario usuario = user.get();

            usuarioRepository.delete(usuario);

            return Optional.of(usuario);
        } else {
            throw new NoAuthorizedException("Acceso denegado!");
        }
    }

    /**
     * <h2>actualizarUsuario</h2>
     * Actualiza los datos un usuario
     * Solo usuarios administradores pueden modificar usuarios varios
     * Solo un usuario puede actualizar los datos de si mismo
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    public Optional<Usuario> actualizarUsuario(UsuarioRequest usuarioRequest) throws Exception {
        if (usuarioRequest.getNombreUsuario().equals(UserDetailsService.UsuarioContext()) ||
                UserDetailsService.IsAdmin()) {

            Optional<Usuario> user =
                    usuarioRepository.findByUsuarioNombre(usuarioRequest.getNombreUsuario());

            if (user.isEmpty()) {
                throw new EntityNotFoundException("No existe un usuario con ese nombre");
            }

            Usuario usuario = user.get();

            List<Usuario> userList =
                    usuarioRepository.findAllByEmail(usuarioRequest.getEmail()).stream()
                            .filter(u -> !u.getUsuarioNombre().equals(usuarioRequest.getNombreUsuario()))
                            .collect(Collectors.toList());

            if (userList.size() != 0) {
                throw new Exception("Ya hay registrado un usuario con ese e-mail");
            }

            if (usuarioRequest.getEmail() != null && !usuarioRequest.getEmail().isEmpty()) {
                usuario.setEmail(usuarioRequest.getEmail());
            }
            if (usuarioRequest.getTelefono() != null && usuarioRequest.getTelefono().isEmpty()) {
                usuario.setEmail(usuarioRequest.getTelefono());
            }
            if (usuarioRequest.getNombre() != null && !usuarioRequest.getNombre().isEmpty()) {
                usuario.setEmail(usuarioRequest.getNombre());
            }
            if (usuarioRequest.getRoles() != null && !usuarioRequest.getRoles().isEmpty()) {
                usuario.setRoles(prepararRoles(usuarioRequest.getRoles()));
            }
            if (usuarioRequest.getClave() != null && !usuarioRequest.getClave().isEmpty()) {
                usuario.setClave(encoder.encode(usuarioRequest.getClave()));
            }

            usuarioRepository.save(usuario);

            return Optional.of(usuario);
        } else {
            throw new NoAuthorizedException("Acceso denegado!");
        }
    }


}

