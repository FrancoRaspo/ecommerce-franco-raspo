package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.models.entities.Orden;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.OrdenRequest;
import com.ecommerce.francoraspo.models.requests.ProductoItemRequest;
import com.ecommerce.francoraspo.services.OrdenService;
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
public class OrdenController {

    @Autowired
    private final OrdenService ordenService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "orden/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> obtenerOrdenById(@PathVariable(name="id") final String id) {
        final Optional<Orden> orden = ordenService.obtenerOrdenById(id);

        if (orden.isPresent()) {
            return ResponseEntity.ok(orden);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "orden/{id}/producto", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?>   obtenerProductosOrdenById(@PathVariable(name="id") final String id) {
        final Optional<Orden> orden = ordenService.obtenerOrdenById(id);

        if (orden.isPresent()) {
            return ResponseEntity.ok(orden.get().getProductoItems());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(value = "orden/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteOrdenById(@PathVariable(name="id") final String id) {
        final Optional<Orden> orden = ordenService.eliminarOrdenById(id);

        if (orden.isPresent()) {
            return ResponseEntity.ok(orden);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "orden", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newOrden(@Valid @RequestBody OrdenRequest ordenRequest){

        try {
            final Optional<Orden> orden = ordenService.nuevaOrden(ordenRequest);
            return ResponseEntity.created(URI.create("")).body(orden.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping(value = "orden/carrito/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newOrdenCarritoById(@PathVariable(name = "id") final String carritoId,
                                                 @Valid @RequestBody OrdenRequest ordenRequest){


        try {
            final Optional<Orden> orden = ordenService.nuevaOrdenByCarritoId(carritoId,ordenRequest);

            return ResponseEntity.created(URI.create("")).body(orden.get());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping(value = "orden/{id}/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateItemOrden(@PathVariable(name = "id") final String id,
                                           @PathVariable(name = "productoId") final String productoId,
                                           @Valid @RequestBody final ProductoItemRequest ordenItemRequest) {

        try {
            Optional<ProductoItem> ordenItemGuardado = ordenService.nuevoProducto(id,
                    productoId,ordenItemRequest.getCantidad());


            if (ordenItemGuardado.isPresent()) {
                return ResponseEntity.ok(ordenItemGuardado.get());
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }


    }

    @DeleteMapping(value = "orden/{id}/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteItemOrden(@PathVariable(name = "id") final String id,
                                           @PathVariable(name = "productoId") final String productoId,
                                           @Valid @RequestBody final ProductoItemRequest ordenItemRequest) {

        try {
            Optional<ProductoItem> ordenItemGuardado = ordenService.eliminarProducto(id,
                    productoId,ordenItemRequest.getCantidad());


            if (ordenItemGuardado.isPresent()) {
                return ResponseEntity.ok(ordenItemGuardado.get());
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
