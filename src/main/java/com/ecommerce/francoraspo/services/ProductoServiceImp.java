package com.ecommerce.francoraspo.services;


import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;
import com.ecommerce.francoraspo.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor

public class ProductoServiceImp implements ProductoService {
    private final ProductoRepository productoRepository;
    @Override
    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Optional<Producto> deleteProductoById(Long id) {
        Optional<Producto> productoDoc = productoRepository.findById(id);
        if (productoDoc.isPresent()) {
            productoRepository.delete(productoDoc.get());
            return productoDoc;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Producto newProducto(ProductoRequest productoRequest) {

        final Producto producto = new Producto();

        producto.setNombre(productoRequest.getNombre());
        producto.setFamilia(productoRequest.getFamilia());
        producto.setMarca(productoRequest.getMarca());

        productoRepository.save(producto);

        return producto;
    }

    @Override
    public Optional<Producto> updateUsuario(Long id, ProductoRequest productoRequest) {

        final Optional<Producto> productoDoc = productoRepository.findById(id);

        if (productoDoc.isPresent()) {
            final Producto producto = productoDoc.get();

            producto.setNombre(productoRequest.getNombre());
            producto.setFamilia(productoRequest.getFamilia());
            producto.setMarca(productoRequest.getMarca());

            productoRepository.save(producto);
            return Optional.of(producto);
        } else {
             return Optional.empty();
        }

    }
}
