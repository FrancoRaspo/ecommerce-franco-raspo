package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.models.entities.*;
import com.ecommerce.francoraspo.models.requests.OrdenRequest;
import com.ecommerce.francoraspo.repositories.CarritoRepository;
import com.ecommerce.francoraspo.repositories.OrdenRepository;
import com.ecommerce.francoraspo.repositories.ProductoRepository;
import com.ecommerce.francoraspo.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class OrdenServiceImp implements OrdenService {
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    private final CarritoRepository carritoRepository;

    @Override

    public Optional<Orden> obtenerOrdenById(String id) {
        return ordenRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Orden> eliminarOrdenById(String id) {
        Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            ordenRepository.delete(ordenDoc.get());
            return ordenDoc;
        } else {
            return Optional.empty();
        }
    }

    private Orden nuevaOrdenUsuario(String nombreUsuario, Carrito carrito) throws EntityNotFoundException {
        final Orden orden = new Orden();

        Optional<Usuario> usuario = usuarioRepository.findByUsuarioNombre(nombreUsuario);

        if (usuario.isEmpty()) {
            throw new EntityNotFoundException("No existe el usuario con el nombre " + nombreUsuario);
        }

        if (carrito != null) {
            if (carrito.getProductoItems().isEmpty() ||
                    carrito.getProductoItems().size() == 0) {
                throw new EntityNotFoundException("El carrito del usuario está vacío");
            }
            orden.setProductoItems(carrito.getProductoItems());
            orden.setCarritoId(carrito.getId());
        }

        orden.setUsuario(nombreUsuario);
        orden.setFechaCompra(LocalDateTime.now());
        orden.setNumeroOrden(ordenRepository.count() + 1);
        ordenRepository.save(orden);
        return orden;

    }

    @Override
    @Transactional
    public Optional<Orden> nuevaOrden(OrdenRequest ordenRequest, String usuarioNombre) throws Exception {

        Orden orden = this.nuevaOrdenUsuario(usuarioNombre, null);

        return Optional.of(orden);
    }

    @Override
    @Transactional
    public Optional<Orden> nuevaOrdenByCarritoUsuarioNombre(OrdenRequest ordenRequest, String usuarioNombre) throws Exception {

        Optional<Carrito> carrito = carritoRepository.findByUsuarioNombre(usuarioNombre);

        if (carrito.isEmpty()) {
            throw new EntityNotFoundException("No hay carrito para el usuario");
        }

        final Orden orden = this.nuevaOrdenUsuario(usuarioNombre, carrito.get());
        return Optional.of(orden);
    }

    @Override
    @Transactional
    public Optional<ProductoItem> nuevoProducto(String id, String productoId, long cantidad) throws EntityNotFoundException {
        final Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            final Orden orden = ordenDoc.get();
            final ProductoItem ordenItem = new ProductoItem();
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);

            if (producto.isEmpty()) {
                throw new EntityNotFoundException("El producto con id " + productoId + " no existe.");
            }

            ordenItem.setProducto(producto.get());
            ordenItem.setCantidad(cantidad);
            ProductoItem ordenItemActualizado = orden.actualizaItem(ordenItem);
            ordenRepository.save(orden);

            return Optional.of(ordenItemActualizado);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<ProductoItem> eliminarProducto(String id, String productoId, long cantidad) throws EntityNotFoundException {
        final Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            final Orden orden = ordenDoc.get();
            final ProductoItem ordenItem = new ProductoItem();
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);

            if (producto.isEmpty()) {
                throw new EntityNotFoundException("El producto con id " + productoId + " no existe.");
            }

            ordenItem.setProducto(producto.get());
            ordenItem.setCantidad(cantidad * -1);

            ProductoItem ordenItemActualizado = orden.actualizaItem(ordenItem);

            ordenRepository.save(orden);

            return Optional.of(ordenItemActualizado);
        } else {
            return Optional.empty();
        }
    }
}
