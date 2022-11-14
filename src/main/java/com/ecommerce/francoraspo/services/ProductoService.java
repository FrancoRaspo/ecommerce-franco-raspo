package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;

import java.util.List;
import java.util.Optional;

public interface ProductoService {
        Optional<Producto> obtenerProductoById(final String id);
        Optional<Producto> eliminarProductoById(final String id);
        Producto nuevoProducto(final ProductoRequest productoRequest);
        Optional<Producto>  actualizarProducto(final String id, final ProductoRequest productoRequest);

        List<Producto> obtenerProductos();

}
