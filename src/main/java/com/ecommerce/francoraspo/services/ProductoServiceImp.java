package com.ecommerce.francoraspo.services;


import com.ecommerce.francoraspo.handlers.exceptions.NoAuthorizedException;
import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;
import com.ecommerce.francoraspo.repositories.ProductoRepository;
import com.ecommerce.francoraspo.services.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <h1>Producto Service</h1>
 * Esta clase administra el documento Producto
 * <p>
 * Solo los usuarios con Rol de administrador pueden administrar productos
 *
 * @author Franco Raspo
 * @version 1.0
 * @since 2022-11-21
 */
@Service
@RequiredArgsConstructor

public class ProductoServiceImp implements ProductoService {
    private final ProductoRepository productoRepository;

    @Override
    /**
     * <h2>obtenerProductoById</h2>
     * Se obtiene un Producto por su ID
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    public Optional<Producto> obtenerProductoById(String id) {
        return productoRepository.findById(id);
    }

    /**
     * <h2>eliminarProductoById</h2>
     * Se elimina un Producto por su ID
     * Solo usuarios Administradores tiene permiso
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    @Transactional
    public Optional<Producto> eliminarProductoById(String id) throws NoAuthorizedException {

        if (!UserDetailsService.IsAdmin()) {
            throw new NoAuthorizedException("No puede eliminar productos si no es administrador");
        }

        Optional<Producto> productoDoc = productoRepository.findById(id);

        if (productoDoc.isPresent()) {
            productoRepository.delete(productoDoc.get());
            return productoDoc;
        } else {
            return Optional.empty();
        }
    }

    /**
     * <h2>nuevoProducto</h2>
     * Genera una nuevo producto
     * Solo usuarios administradores tiene autorización
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    @Transactional
    public Producto nuevoProducto(ProductoRequest productoRequest) throws NoAuthorizedException {
        if (!UserDetailsService.IsAdmin()) {
            throw new NoAuthorizedException("No puede crear productos si no es administrador");
        }

        final Producto producto = new Producto();

        producto.setCodigo(productoRequest.getCodigo());
        producto.setCategoria(productoRequest.getCategoria());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setPrecioUnitario(productoRequest.getPrecioUnitario());

        productoRepository.save(producto);

        return producto;
    }

    /**
     * <h2>actualizarProducto</h2>
     * Actualiza un nuevo producto
     * Solo usuarios administradores tiene autorización
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    @Transactional
    public Optional<Producto> actualizarProducto(String id, ProductoRequest productoRequest) throws NoAuthorizedException {

        if (!UserDetailsService.IsAdmin()) {
            throw new NoAuthorizedException("No puede modificar productos si no es admnistrador");
        }

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

    /**
     * <h2>obtenerProductos</h2>
     * Obtiene una lista de productos
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }
}
