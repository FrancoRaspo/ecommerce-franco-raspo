package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.models.entities.*;
import com.ecommerce.francoraspo.models.enums.EOrdenEstado;
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
/**
 * <h1>Orden Service</h1>
 * Esta clase administra el documento Orden
 * <p>
 *  Los Carritos se convierten en Ordenes generadas
 *  El usuario puede cerrar su orden.
 * @author  Franco Raspo
 * @version 1.0
 * @since   2022-11-21
 */
@Service
@RequiredArgsConstructor

public class OrdenServiceImp implements OrdenService {
    private final OrdenRepository ordenRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    private final CarritoRepository carritoRepository;

    /**
     * <h2>obtenerOrdenById</h2>
     * Se obtiene un documento Orden por su ID
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    public Optional<Orden> obtenerOrdenById(String id) {
        return ordenRepository.findById(id);
    }

    /**
     * <h2>eliminarOrdenById</h2>
     * Se elimina un documento Orden por ID
     * @author  Franco Raspo
     * @since   2022-11-21
     */
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

    /**
     * <h2>nuevaOrdenUsuario</h2>
     * Genera una nueva orden para un usuario
     * Puede recibir un documento Carrito para generar
     * El número de orden se obtiene por la ultima orden cargado + 1
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    private Orden nuevaOrdenUsuario(String nombreUsuario, Carrito carrito) throws EntityNotFoundException {
        final Orden orden = new Orden();

        //busco que el usuario exista
        Optional<Usuario> usuario = usuarioRepository.findByUsuarioNombre(nombreUsuario);
        if (usuario.isEmpty()) {
            throw new EntityNotFoundException("No existe el usuario con el nombre " + nombreUsuario);
        }

        //Si envía un carrito tomo los productos y los paso a la orden
        if (carrito != null) {
            if (carrito.getProductoItems().isEmpty() ||
                    carrito.getProductoItems().size() == 0) {
                throw new EntityNotFoundException("El carrito del usuario está vacío");
            }
            orden.setProductoItems(carrito.getProductoItems());
            orden.setCarrito(carrito);
        }

        //valores de la orden nueva
        orden.setUsuario(nombreUsuario);
        orden.setFechaCompra(LocalDateTime.now());

        //Se busca la una orden ordenada por numeroOrden
        Optional<Orden> ordenUltima = ordenRepository.findFirstByOrderByNumeroOrdenDesc();
        orden.setNumeroOrden(ordenUltima.map(value -> value.getNumeroOrden() + 1).orElse(1l));
        orden.setEstado(EOrdenEstado.GENERADA);
        ordenRepository.save(orden);

        //Una vez que genera la orden del carrito lo elimino
        if (carrito != null) {
            carritoRepository.delete(carrito);
        }
        return orden;

    }

    /**
     * <h2>nuevaOrden</h2>
     * Genera una nueva orden para un usuario sin carrito
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    @Transactional
    public Optional<Orden> nuevaOrden(OrdenRequest ordenRequest, String usuarioNombre) throws Exception {

        Orden orden = this.nuevaOrdenUsuario(usuarioNombre, null);

        return Optional.of(orden);
    }

    /**
     * <h2>nuevaOrdenByCarritoUsuarioNombre</h2>
     * Genera una nueva orden para un usuario con carrito
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    @Transactional
    public Optional<Orden> nuevaOrdenByCarritoUsuarioNombre(String usuarioNombre) throws Exception {

        //debe tener un carrito el usuario
        Optional<Carrito> carrito = carritoRepository.findByUsuarioNombre(usuarioNombre);
        if (carrito.isEmpty()) {
            throw new EntityNotFoundException("No hay carrito para el usuario");
        }

        final Orden orden = this.nuevaOrdenUsuario(usuarioNombre, carrito.get());
        return Optional.of(orden);
    }

    /**
     * <h2>cerrarOrdenById</h2>
     * Cierra  una orden por ID
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    public Optional<Orden> cerrarOrdenById(String id) throws Exception {
        Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            Orden orden = ordenDoc.get();

            if (!orden.getEstado().equals(EOrdenEstado.GENERADA)) {
                throw new EntityNotFoundException("La orden no esta abierta.");
            }

            orden.setEstado(EOrdenEstado.VENDIDA);

            ordenRepository.save(orden);
            return ordenDoc;
        } else {
            throw new EntityNotFoundException("No existe la orden con Id " + id);
        }
    }

    /**
     * <h2>nuevoProducto</h2>
     * Agrega un producto por Id a la orden por Id
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    @Transactional
    public Optional<ProductoItem> nuevoProducto(String id, String productoId, long cantidad) throws EntityNotFoundException {

        //Busco la orden
        final Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            final Orden orden = ordenDoc.get();
            final ProductoItem ordenItem = new ProductoItem();
            //Busco el producto
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);
            if (producto.isEmpty()) {
                throw new EntityNotFoundException("El producto con id " + productoId + " no existe.");
            }

            //Asigno los valor del producto y agrega a la orden
            ordenItem.setProducto(producto.get());
            ordenItem.setCantidad(cantidad);
            ProductoItem ordenItemActualizado = orden.actualizaItem(ordenItem);
            ordenRepository.save(orden);

            return Optional.of(ordenItemActualizado);
        } else {
            return Optional.empty();
        }
    }
    /**
     * <h2>eliminarProducto</h2>
     * Elimina un producto por Id a la orden por Id
     * @author  Franco Raspo
     * @since   2022-11-21
     */
    @Override
    @Transactional
    public Optional<ProductoItem> eliminarProducto(String id, String productoId, long cantidad) throws EntityNotFoundException {
        //Busco la orden
        final Optional<Orden> ordenDoc = ordenRepository.findById(id);
        if (ordenDoc.isPresent()) {
            final Orden orden = ordenDoc.get();
            final ProductoItem ordenItem = new ProductoItem();

            //Busco el producto
            final Optional<Producto> producto = productoRepository
                    .findById(productoId);
            if (producto.isEmpty()) {
                throw new EntityNotFoundException("El producto con id " + productoId + " no existe.");
            }

            ordenItem.setProducto(producto.get());
            //Asigno la cantidad en negativo para disminuir
            ordenItem.setCantidad(cantidad * -1);

            ProductoItem ordenItemActualizado = orden.actualizaItem(ordenItem);

            ordenRepository.save(orden);

            return Optional.of(ordenItemActualizado);
        } else {
            return Optional.empty();
        }
    }
}
