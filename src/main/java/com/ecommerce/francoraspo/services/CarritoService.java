package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.Carrito;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.CarritoRequest;

import java.util.Optional;

public interface CarritoService {
        Optional<Carrito> obtenerCarritoById(final String id);
        Optional<Carrito> eliminarCarritoById(final String id);
        Optional<Carrito> nuevoCarrito(final CarritoRequest carritoRequest);
        Optional<ProductoItem>  nuevoProducto(final String id, final String productoId,
                                              final long cantidad) throws Exception;
        Optional<ProductoItem> eliminarProducto(final String id, final String productoId,
                                                final long cantidad) throws Exception;

}
