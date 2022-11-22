package com.ecommerce.francoraspo.repositories;


import com.ecommerce.francoraspo.models.entities.Orden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenRepository extends MongoRepository<Orden,String> {

    Optional<Orden> findFirstByOrderByNumeroOrdenDesc();
}
