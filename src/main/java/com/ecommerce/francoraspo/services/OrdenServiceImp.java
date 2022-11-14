package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.*;
import com.ecommerce.francoraspo.models.requests.OrdenRequest;
import com.ecommerce.francoraspo.repositories.OrdenRepository;
import com.ecommerce.francoraspo.repositories.ProductoRepository;
import com.ecommerce.francoraspo.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class OrdenServiceImp implements OrdenService{
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarritoService carritoService;

    @Override

    public Optional<Orden> obtenerOrdenById(String id) {
        Optional<Orden> ordenDoc = ordenRepository.findById(id);
        return ordenDoc;
    }

    @Override
    public Optional<Orden> eliminarOrdenById(String id) {
        Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            ordenRepository.delete(ordenDoc.get());
            return ordenDoc;
        } else {
            return Optional.empty();
        }
    }

    private Orden nuevaOrdenUsuario(String nombreUsuario, String carritoId) throws Exception {
        final Orden orden = new Orden();

        Optional<Usuario> usuario = usuarioRepository.findByUsuario(nombreUsuario);

        if (usuario.isEmpty()) {
            throw new Exception("No existe el usuario con el nombre " + nombreUsuario);
        }


        if (carritoId != null) {
            Optional<Carrito> carrito = carritoService.obtenerCarritoById(carritoId);

            if (carrito.isEmpty()) {
                throw new Exception("El carrito con ID " + carritoId + "no existe.");
            }
            orden.setProductoItems(carrito.get().getProductoItems());
            orden.setCarritoId(carrito.get().getId());
        }

        orden.setUsuario(nombreUsuario);
        orden.setFechaCompra(LocalDateTime.now());
        orden.setNumeroOrden(ordenRepository.count()+1);
        ordenRepository.save(orden);
        return orden;

    }
    @Override
    public Optional<Orden> nuevaOrden(OrdenRequest ordenRequest) throws Exception {

        Orden orden = this.nuevaOrdenUsuario(ordenRequest.getUsuario(),null);

        return Optional.of(orden);
    }

    @Override
    public Optional<Orden> nuevaOrdenByCarritoId(String carritoId, OrdenRequest ordenRequest) throws Exception {
        final Orden orden = this.nuevaOrdenUsuario(ordenRequest.getUsuario(), carritoId);
        return Optional.of(orden);
    }



    @Override
    public Optional<ProductoItem> nuevoProducto(String id, String productoId, long cantidad) throws Exception {
        final Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            final Orden orden = ordenDoc.get();
            final ProductoItem ordenItem= new ProductoItem();
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);

            if (producto.isEmpty()) {
                throw new Exception("El producto con id " + productoId + " no existe.");
            }

            ordenItem.setProducto(producto.get());
            ordenItem.setCantidad(cantidad);
            ProductoItem ordenItemActualizado =  orden.actualizaItem(ordenItem);
            ordenRepository.save(orden);

            return Optional.of(ordenItemActualizado);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductoItem> eliminarProducto(String id, String productoId, long cantidad) throws Exception {
        final Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            final Orden orden = ordenDoc.get();
            final ProductoItem ordenItem= new ProductoItem();
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);

            if (producto.isEmpty()) {
                throw new Exception("El producto con id " + productoId + " no existe.");
            }

            ordenItem.setProducto(producto.get());
            ordenItem.setCantidad(cantidad*-1);

            ProductoItem ordenItemActualizado =  orden.actualizaItem(ordenItem);

            ordenRepository.save(orden);

            return Optional.of(ordenItemActualizado);
        } else {
            return Optional.empty();
        }
    }
}
