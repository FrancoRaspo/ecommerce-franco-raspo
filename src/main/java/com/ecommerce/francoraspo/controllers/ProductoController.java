package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.models.entities.Producto;
import com.ecommerce.francoraspo.models.requests.ProductoRequest;
import com.ecommerce.francoraspo.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductoController {
    private static final String template = "Hola, %s!";

    @Autowired
    private final ProductoService productoService;


    @GetMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProductoById(@PathVariable(name="id") final Long id) {
        final Optional<Producto> producto = productoService.getProductoById(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteProductoById(@PathVariable(name="id") final Long id) {
        final Optional<Producto> producto = productoService.deleteProductoById(id);

        if (producto.isPresent()) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "producto", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newProducto(@RequestBody ProductoRequest productoRequest){

        try {
            final Producto producto = productoService.newProducto(productoRequest);
            return ResponseEntity.created(URI.create("")).body(producto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @PutMapping(value = "producto/{id}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateProducto(@PathVariable(name = "id") final Long id,
                                               @RequestBody final ProductoRequest productoRequest) {

        final Optional<Producto> producto = productoService.getProductoById(id);

        if (producto.isPresent()) {
            try {
                final Optional<Producto> productoGuardado = productoService.updateUsuario(id, productoRequest);
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
