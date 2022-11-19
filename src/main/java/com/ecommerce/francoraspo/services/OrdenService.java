package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.Orden;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.OrdenRequest;

import java.util.Optional;

public interface OrdenService {
    Optional<Orden> obtenerOrdenById(final String id);

    Optional<Orden> eliminarOrdenById(final String id);

    Optional<Orden> nuevaOrden(final OrdenRequest ordenRequest, final String usuarioNombre) throws Exception;

    Optional<Orden> nuevaOrdenByCarritoUsuarioNombre(
            final OrdenRequest ordenRequest,
            final String usuarioNombre) throws Exception;

    Optional<ProductoItem> nuevoProducto(final String id, final String productoId,
                                         final long cantidad) throws Exception;

    Optional<ProductoItem> eliminarProducto(final String id, final String productoId,
                                            final long cantidad) throws Exception;

}

