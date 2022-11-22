package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.models.entities.Carrito;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.CarritoRequest;
import com.ecommerce.francoraspo.models.requests.ProductoItemRequest;
import com.ecommerce.francoraspo.models.responses.Response;
import com.ecommerce.francoraspo.services.CarritoService;
import com.ecommerce.francoraspo.services.security.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CarritoController {

    @Autowired
    private final CarritoService carritoService;

    Logger logger = LogManager.getLogger(CarritoController.class);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "carrito", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> obtenerCarritoByUsuarioNombre() throws EntityNotFoundException {
        logger.info("GET /api/carrito/{id}");
        final Optional<Carrito> carrito = carritoService.obtenerCarritoByUsuarioNombre(UserDetailsService.UsuarioContext());
        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito);
        } else {
            throw new EntityNotFoundException("No hay carrito para el usuario.");
        }
    }

    @GetMapping(value = "carrito/producto", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> obtenerProductosCarritoById() throws EntityNotFoundException {
        logger.info("GET /api/carrito/producto");
        final Optional<Carrito> carrito = carritoService.obtenerCarritoByUsuarioNombre(UserDetailsService.UsuarioContext());
        if (carrito.isPresent()) {
            return ResponseEntity.ok(carrito.get().getProductoItems());
        } else {
            throw new EntityNotFoundException("No hay carrito para el usuario");
        }
    }

    @DeleteMapping(value = "carrito", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteCarritoById() throws EntityNotFoundException {
        logger.info("DELETE /api/carrito");
        final Optional<Carrito> carrito = carritoService.eliminarCarritoByUsuarioNombre(UserDetailsService.UsuarioContext());

        if (carrito.isPresent()) {
            final Response<ProductoItem> result = new Response(Instant.now(), carrito.get(),
                    HttpStatus.OK.value(), "Success");
            return new ResponseEntity<>(result, HttpStatus.OK);


        } else {
            throw new EntityNotFoundException("No hay carrito para el usuario");
        }
    }

    @PostMapping(value = "carrito", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newCarrito(@Valid @RequestBody CarritoRequest carritoRequest) throws ApiRestException {

        logger.info("POST /api/carrito");
        Optional<Carrito> carrito = carritoService.obtenerCarritoByUsuarioNombre(UserDetailsService.UsuarioContext());

        if (carrito.isPresent()) {
            throw new ApiRestException("Ya hay un carrito para el usuario");
        }
        try {
            Optional<Carrito> carritoNuevo = carritoService.nuevoCarrito(carritoRequest, UserDetailsService.UsuarioContext());
            if (carritoNuevo.isPresent()) {
                final Response<ProductoItem> result = new Response(Instant.now(), carritoNuevo.get(),
                        HttpStatus.CREATED.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.CREATED);
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

        logger.info("POST /api/carrito/producto/{productoId}");

        try {
            Optional<ProductoItem> carritoItemGuardado = carritoService.nuevoProductoCarritoUsuario(
                    UserDetailsService.UsuarioContext(),
                    productoId, carritoItemRequest.getCantidad());
            if (carritoItemGuardado.isPresent()) {
                final Response<ProductoItem> result = new Response(Instant.now(),
                        carritoItemGuardado, HttpStatus.CREATED.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.CREATED);
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
        logger.info("DELETE /carrito/producto/{productoId}");

        try {
            Optional<ProductoItem> carritoItemGuardado = carritoService.eliminarProductoCarritoUsuario(
                    UserDetailsService.UsuarioContext(), productoId, carritoItemRequest.getCantidad());

            if (carritoItemGuardado.isPresent()) {
                final Response<ProductoItem> result = new Response(Instant.now(), carritoItemGuardado,
                        HttpStatus.OK.value(), "Success");
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
