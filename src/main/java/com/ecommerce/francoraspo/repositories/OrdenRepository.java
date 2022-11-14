package com.ecommerce.francoraspo.repositories;


import com.ecommerce.francoraspo.models.entities.Orden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends MongoRepository<Orden,String> {

}
