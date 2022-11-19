package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;
import com.ecommerce.francoraspo.models.responses.Response;
import com.ecommerce.francoraspo.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductoController {

    @Autowired
    private final ProductoService productoService;

    Logger logger = LogManager.getLogger(ProductoController.class);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProductoById(@PathVariable(name = "id") final String id) throws EntityNotFoundException {
        logger.info("GET /api/producto/{id}");
        final Optional<Producto> producto = productoService.obtenerProductoById(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto);
        } else {
            logger.info("Entidad no encontrada /api/producto/" + id);
            throw new EntityNotFoundException("El producto con ID " + id + " no existe.");
        }
    }

    @GetMapping(value = "producto", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProductos() throws EntityNotFoundException {
        logger.info("GET /api/producto");
        final List<Producto> productos = productoService.obtenerProductos();


        if (!productos.isEmpty()) {
            return ResponseEntity.ok(productos);
        } else {
            logger.info("No hay productos cargados");
            throw new EntityNotFoundException("No hay productos cargados");
        }
    }

    @DeleteMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteProductoById(@PathVariable(name = "id") final String id) throws EntityNotFoundException {
        logger.info("DELETE /api/producto/{id}");

        final Optional<Producto> producto = productoService.eliminarProductoById(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto);
        } else {
            logger.info("Entidad no encontrada /api/producto/" + id);
            throw new EntityNotFoundException("El producto con ID " + id + " no existe.");
        }
    }

    @PostMapping(value = "producto", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newProducto(@Valid @RequestBody ProductoRequest productoRequest) throws ApiRestException {
        logger.info("POST /api/producto");
        logger.debug(productoRequest);


        try {
            final Producto producto = productoService.nuevoProducto(productoRequest);
            return ResponseEntity.created(URI.create("")).body(producto);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }
    }

    @PutMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateProducto(@PathVariable(name = "id") final String id,
                                            @Valid @RequestBody final ProductoRequest productoRequest) throws ApiRestException, EntityNotFoundException {
        logger.info("PUT /api/producto/{id}");
        logger.debug(productoRequest);

        final Optional<Producto> producto = productoService.obtenerProductoById(id);

        if (producto.isPresent()) {
            try {
                final Optional<Producto> productoGuardado = productoService.actualizarProducto(id, productoRequest);
                if (productoGuardado.isPresent()) {
                    final Response<ProductoItem> result = new Response(Instant.now(), productoGuardado, 200, "Success");
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    throw new ApiRestException("No se pudo actualizar el producto " + id);
                }
            } catch (Exception e) {
                throw new ApiRestException(e.getMessage());
            }
        } else {
            throw new EntityNotFoundException("El producto con ID " + id + " no existe.");
        }

    }
}
