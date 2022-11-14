package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.models.entities.Carrito;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.ProductoItemRequest;
import com.ecommerce.francoraspo.models.requests.CarritoRequest;
import com.ecommerce.francoraspo.services.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CarritoController {

    @Autowired
    private final CarritoService carritoService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "carrito/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?>   obtenerCarritoById(@PathVariable(name="id") final String id) {
        final Optional<Carrito> carrito = carritoService.obtenerCarritoById(id);

        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "carrito/{id}/producto", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?>   obtenerProductosCarritoById(@PathVariable(name="id") final String id) {
        final Optional<Carrito> carrito = carritoService.obtenerCarritoById(id);

        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito.get().getProductoItems());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(value = "carrito/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteCarritoById(@PathVariable(name="id") final String id) {
        final Optional<Carrito> carrito = carritoService.eliminarCarritoById(id);

        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "carrito", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newCarrito(@Valid @RequestBody CarritoRequest carritoRequest){

        try {
            final Optional<Carrito> carrito = carritoService.nuevoCarrito(carritoRequest);
            return ResponseEntity.created(URI.create("")).body(carrito.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @PostMapping(value = "carrito/{id}/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateItemCarrito(@PathVariable(name = "id") final String id,
                                           @PathVariable(name = "productoId") final String productoId,
                                           @Valid @RequestBody final ProductoItemRequest carritoItemRequest) {

        try {
            Optional<ProductoItem> carritoItemGuardado = carritoService.nuevoProducto(id,
                    productoId,carritoItemRequest.getCantidad());


            if (carritoItemGuardado.isPresent()) {
                return ResponseEntity.ok(carritoItemGuardado.get());
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }


    }

    @DeleteMapping(value = "carrito/{id}/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteItemCarrito(@PathVariable(name = "id") final String id,
                                           @PathVariable(name = "productoId") final String productoId,
                                           @Valid @RequestBody final ProductoItemRequest carritoItemRequest) {

        try {
            Optional<ProductoItem> carritoItemGuardado = carritoService.eliminarProducto(id,
                    productoId,carritoItemRequest.getCantidad());


            if (carritoItemGuardado.isPresent()) {
                return ResponseEntity.ok(carritoItemGuardado.get());
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }


    }

}
