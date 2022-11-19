package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.models.authJwtModels.UserDetailsImpl;
import com.ecommerce.francoraspo.models.entities.Carrito;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.CarritoRequest;
import com.ecommerce.francoraspo.models.requests.ProductoItemRequest;
import com.ecommerce.francoraspo.models.responses.Response;
import com.ecommerce.francoraspo.services.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CarritoController {

    @Autowired
    private final CarritoService carritoService;


    private String UsuarioCarrito() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "carrito", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> obtenerCarritoByUsuarioNombre() throws EntityNotFoundException {
        final Optional<Carrito> carrito = carritoService.obtenerCarritoByUsuarioNombre(UsuarioCarrito());
        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito);
        } else {
            throw new EntityNotFoundException("No hay carrito para el usuario.");
        }
    }

    @GetMapping(value = "carrito/producto", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> obtenerProductosCarritoById() throws EntityNotFoundException {
        final Optional<Carrito> carrito = carritoService.obtenerCarritoByUsuarioNombre(UsuarioCarrito());

        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito.get().getProductoItems());
        } else {
            throw new EntityNotFoundException("No hay carrito para el usuario");
        }
    }

    @DeleteMapping(value = "carrito", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteCarritoById() throws EntityNotFoundException {
        final Optional<Carrito> carrito = carritoService.eliminarCarritoByUsuarioNombre(UsuarioCarrito());

        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito);
        } else {
            throw new EntityNotFoundException("No hay carrito para el usuario");
        }
    }

    @PostMapping(value = "carrito", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newCarrito(@Valid @RequestBody CarritoRequest carritoRequest) throws ApiRestException {
        Optional<Carrito> carrito = carritoService.obtenerCarritoByUsuarioNombre(UsuarioCarrito());

        if (carrito.isPresent()) {
            throw new ApiRestException("Ya hay un carrito para el usuario");
        }
        try {
            Optional<Carrito>  carritoNuevo = carritoService.nuevoCarrito(carritoRequest, UsuarioCarrito());
            if (carritoNuevo.isPresent()) {
                return ResponseEntity.created(URI.create("")).body(carritoNuevo.get());
            }
            throw new ApiRestException("Ocurrió un error al crear el carrito.");
        } catch (Exception e) {
            throw new ApiRestException(e.getMessage());
        }
    }

    @PostMapping(value = "carrito/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateItemCarrito(@PathVariable(name = "productoId") final String productoId,
                                               @Valid @RequestBody final ProductoItemRequest carritoItemRequest)
            throws ApiRestException, EntityNotFoundException {


        try {
            Optional<ProductoItem> carritoItemGuardado = carritoService.nuevoProductoCarritoUsuario(UsuarioCarrito(),
                    productoId, carritoItemRequest.getCantidad());
            if (carritoItemGuardado.isPresent()) {
                final Response<ProductoItem> result = new Response(Instant.now(),
                        carritoItemGuardado, 200, "Success");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                throw new ApiRestException("No se pudo ingresar el producto " + productoId + " al carrito.");
            }
        } catch (EntityNotFoundException nfe) {
            throw nfe;
        } catch (Exception e) {
            throw new ApiRestException(e.getMessage());
        }
    }

    @DeleteMapping(value = "carrito/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteItemCarrito(
            @PathVariable(name = "productoId") final String productoId,
            @Valid @RequestBody final ProductoItemRequest carritoItemRequest) throws EntityNotFoundException, ApiRestException {

        try {
            Optional<ProductoItem> carritoItemGuardado = carritoService.eliminarProductoCarritoUsuario(
                    UsuarioCarrito(), productoId, carritoItemRequest.getCantidad());

            if (carritoItemGuardado.isPresent()) {
                final Response<ProductoItem> result = new Response(Instant.now(), carritoItemGuardado,
                        200, "Success");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                throw new ApiRestException("No se pudo eliminar el producto " + productoId + " al carrito.");
            }
        } catch (EntityNotFoundException nfe) {
            throw nfe;
        } catch (Exception e) {
            throw new ApiRestException(e.getMessage());
        }


    }

}
