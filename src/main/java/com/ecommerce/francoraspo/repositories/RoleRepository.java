package com.ecommerce.francoraspo.repositories;


import com.ecommerce.francoraspo.models.authJwtModels.RolSeguridad;
import com.ecommerce.francoraspo.models.enums.ERolSeguridad;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<RolSeguridad, String> {
    Optional<RolSeguridad> findByName(ERolSeguridad name);
}

