package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.entities.Carrito;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.CarritoRequest;
import com.ecommerce.francoraspo.repositories.CarritoRepository;
import com.ecommerce.francoraspo.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CarritoServiceImp  implements CarritoService{
    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    @Override

    public Optional<Carrito> obtenerCarritoById(String id) {
        Optional<Carrito> carritoDoc = carritoRepository.findById(id);
        return carritoDoc;
    }

    @Override
    public Optional<Carrito> eliminarCarritoById(String id) {
        Optional<Carrito> carritoDoc = carritoRepository.findById(id);
        if (carritoDoc.isPresent()) {
            carritoRepository.delete(carritoDoc.get());
            return carritoDoc;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Carrito> nuevoCarrito(CarritoRequest carritoRequest) {
        final Carrito carrito = new Carrito();

        carrito.setDireccionEntrega(carritoRequest.getDireccionEntrega());
        carrito.setFechaHora(LocalDateTime.now());
        carrito.setProductoItems(carritoRequest.getProductoItems());

        carritoRepository.save(carrito);

        return Optional.of(carrito);
    }

    @Override
    public Optional<ProductoItem> nuevoProducto(String id, String productoId, long cantidad) throws Exception {
        final Optional<Carrito> carritoDoc = carritoRepository.findById(id);
        if (carritoDoc.isPresent()) {
            final Carrito carrito = carritoDoc.get();
            final ProductoItem carritoItem= new ProductoItem();
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);

            if (producto.isEmpty()) {
                throw new Exception("El producto con Id " + productoId + " no existe");
            }


            carritoItem.setProducto(producto.get());
            carritoItem.setCantidad(cantidad);
            ProductoItem carritoItemActualizado =  carrito.actualizaItem(carritoItem);
            carritoRepository.save(carrito);

            return Optional.of(carritoItemActualizado);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ProductoItem> eliminarProducto(String id, String productoId, long cantidad) throws Exception {
        final Optional<Carrito> carritoDoc = carritoRepository.findById(id);
        if (carritoDoc.isPresent()) {
            final Carrito carrito = carritoDoc.get();
            final ProductoItem carritoItem= new ProductoItem();
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);

            if (producto.isEmpty()) {
                throw new Exception("El producto con Id " + productoId + " no existe");
            }

            carritoItem.setProducto(producto.get());
            carritoItem.setCantidad(cantidad*-1);

            ProductoItem carritoItemActualizado =  carrito.actualizaItem(carritoItem);

            carritoRepository.save(carrito);

            return Optional.of(carritoItemActualizado);
        } else {
            return Optional.empty();
        }
    }
}
