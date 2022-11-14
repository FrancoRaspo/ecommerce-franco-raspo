package com.ecommerce.francoraspo.services;


import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;
import com.ecommerce.francoraspo.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ProductoServiceImp implements ProductoService {
    private final ProductoRepository productoRepository;
    @Override
    public Optional<Producto> obtenerProductoById(String id) {
        return productoRepository.findById(id);
    }

    @Override
    public Optional<Producto> eliminarProductoById(String id) {
        Optional<Producto> productoDoc = productoRepository.findById(id);
        if (productoDoc.isPresent()) {
            productoRepository.delete(productoDoc.get());
            return productoDoc;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Producto nuevoProducto(ProductoRequest productoRequest) {

        final Producto producto = new Producto();

        producto.setCodigo(productoRequest.getCodigo());
        producto.setCategoria(productoRequest.getCategoria());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setPrecioUnitario(productoRequest.getPrecioUnitario());

        productoRepository.save(producto);

        return producto;
    }

    @Override
    public Optional<Producto> actualizarProducto(String id, ProductoRequest productoRequest) {

        final Optional<Producto> productoDoc = productoRepository.findById(id);

        if (productoDoc.isPresent()) {
            final Producto producto = productoDoc.get();

            producto.setCodigo(productoRequest.getCodigo());
            producto.setCategoria(productoRequest.getCategoria());
            producto.setDescripcion(productoRequest.getDescripcion());
            producto.setPrecioUnitario(productoRequest.getPrecioUnitario());

            productoRepository.save(producto);
            return Optional.of(producto);
        } else {
             return Optional.empty();
        }

    }

    @Override
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }
}
