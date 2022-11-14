package com.ecommerce.francoraspo.repositories;


import com.ecommerce.francoraspo.models.entities.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario,String> {
    Optional<Usuario> findByUsuario(String usuario);
}
