package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;

import java.util.Optional;

public interface ProductoService {
        Optional<Producto> getProductoById(final Long id);
        Optional<Producto> deleteProductoById(final Long id);
        Producto newProducto(final ProductoRequest productoRequest);
        Optional<Producto>  updateUsuario(final Long id, final ProductoRequest productoRequest);
}
