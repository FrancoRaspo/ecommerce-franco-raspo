package com.ecommerce.francoraspo.repositories;


import com.ecommerce.francoraspo.models.entities.Carrito;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoRepository extends MongoRepository<Carrito,String> {

}
