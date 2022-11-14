package com.ecommerce.francoraspo.repositories;


import com.ecommerce.francoraspo.models.entities.Producto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends MongoRepository<Producto,String> {

}
