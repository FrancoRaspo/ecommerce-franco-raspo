package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.handlers.exceptions.NoAuthorizedException;
import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
        Optional<Producto> obtenerProductoById(final String id);
        Optional<Producto> eliminarProductoById(final String id) throws NoAuthorizedException;
        Producto nuevoProducto(final ProductoRequest productoRequest) throws NoAuthorizedException;
        Optional<Producto>  actualizarProducto(final String id, final ProductoRequest productoRequest) throws NoAuthorizedException;

        List<Producto> obtenerProductos();

}
