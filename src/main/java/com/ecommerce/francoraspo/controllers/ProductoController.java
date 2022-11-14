package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;
import com.ecommerce.francoraspo.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductoController {

    @Autowired
    private final ProductoService productoService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProductoById(@PathVariable(name="id") final String id) {
        final Optional<Producto> producto = productoService.obtenerProductoById(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "producto", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProductos() {
        final List<Producto> productos = productoService.obtenerProductos();

        if (!productos.isEmpty()) {
            return ResponseEntity.ok(productos);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteProductoById(@PathVariable(name="id") final String id) {
        final Optional<Producto> producto = productoService.eliminarProductoById(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "producto", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newProducto(@Valid @RequestBody ProductoRequest productoRequest){

        try {
            final Producto producto = productoService.nuevoProducto(productoRequest);
            return ResponseEntity.created(URI.create("")).body(producto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @PutMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateProducto(@PathVariable(name = "id") final String id,
                                               @RequestBody final ProductoRequest productoRequest) {

        final Optional<Producto> producto = productoService.obtenerProductoById(id);

        if (producto.isPresent()) {
            try {
                final Optional<Producto> productoGuardado = productoService.actualizarProducto(id, productoRequest);
                if (productoGuardado.isPresent()) {
                    return ResponseEntity.ok(productoGuardado.get());
                }
                else
                {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.internalServerError().body(null);
            }
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}
