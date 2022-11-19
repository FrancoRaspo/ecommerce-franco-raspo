package com.ecommerce.francoraspo.repositories;


import com.ecommerce.francoraspo.models.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, Long> {
    Optional<Usuario> findByUsuarioNombre(String usuarioNombre);

    Boolean existsByUsuarioNombre(String usuarioNombre);

    Boolean existsByEmail(String email);

    List<Usuario> findAllByEmail(String email);
}


