package com.ecommerce.francoraspo.controllers;

import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.models.entities.Orden;
import com.ecommerce.francoraspo.models.entities.ProductoItem;
import com.ecommerce.francoraspo.models.requests.OrdenRequest;
import com.ecommerce.francoraspo.models.requests.ProductoItemRequest;
import com.ecommerce.francoraspo.models.responses.Response;
import com.ecommerce.francoraspo.services.OrdenService;
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
public class OrdenController {

    @Autowired
    private final OrdenService ordenService;

    Logger logger = LogManager.getLogger(OrdenController.class);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "orden/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> obtenerOrdenById(@PathVariable(name = "id") final String id) throws EntityNotFoundException {
        logger.info("GET /api/orden/{id}");

        final Optional<Orden> orden = ordenService.obtenerOrdenById(id);

        if (orden.isPresent()) {
            return ResponseEntity.ok(orden);
        } else {
            logger.info("Entidad no encontrada /api/orden/" + id);
            throw new EntityNotFoundException("La orden con ID " + id + " no existe.");
        }
    }

    @GetMapping(value = "orden/{id}/producto", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> obtenerProductosOrdenById(@PathVariable(name = "id") final String id)
            throws EntityNotFoundException {
        logger.info("GET /api/orden/{id}/producto");
        final Optional<Orden> orden = ordenService.obtenerOrdenById(id);

        if (orden.isPresent()) {
            return ResponseEntity.ok(orden.get().getProductoItems());
        } else {
            logger.info("Entidad no encontrada /api/orden/" + id);
            throw new EntityNotFoundException("La orden con ID " + id + " no existe.");
        }

    }

    @DeleteMapping(value = "orden/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteOrdenById(@PathVariable(name = "id") final String id) throws EntityNotFoundException {
        logger.info("DELETE /api/orden/{id}");
        final Optional<Orden> orden = ordenService.eliminarOrdenById(id);

        if (orden.isPresent()) {
            final Response<ProductoItem> result = new Response(Instant.now(), orden.get(),
                    HttpStatus.OK.value(), "Success");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            logger.info("Entidad no encontrada /api/orden/" + id);
            throw new EntityNotFoundException("La orden con ID " + id + " no existe.");
        }
    }

    @PostMapping(value = "orden", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> newOrden(@Valid @RequestBody OrdenRequest ordenRequest) throws ApiRestException {
        logger.info("POST /api/orden");
        logger.debug(ordenRequest);

        try {
            final Optional<Orden> orden = ordenService.nuevaOrden(ordenRequest, UserDetailsService.UsuarioContext());
            final Response<ProductoItem> result = new Response(Instant.now(), orden.get(),
                    HttpStatus.CREATED.value(), "Success");
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }
    }

    @PostMapping(value = "orden/carrito")
    public ResponseEntity<?> newOrdenCarrito() throws ApiRestException {
        logger.info("POST /api/orden/carrito");

        try {
            final Optional<Orden> orden = ordenService.nuevaOrdenByCarritoUsuarioNombre(UserDetailsService.UsuarioContext());
            final Response<ProductoItem> result = new Response(Instant.now(), orden.get(),
                    HttpStatus.CREATED.value(), "Success");
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }
    }

    @PostMapping(value = "orden/{id}/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> ingresarItemOrden(@PathVariable(name = "id") final String id,
                                               @PathVariable(name = "productoId") final String productoId,
                                               @Valid @RequestBody final ProductoItemRequest ordenItemRequest)
            throws EntityNotFoundException, ApiRestException {
        logger.info("POST /api/orden/{id}/producto/{productoId}");
        logger.debug(ordenItemRequest);

        try {
            Optional<ProductoItem> ordenItemGuardado = ordenService.nuevoProducto(id,
                    productoId, ordenItemRequest.getCantidad());


            if (ordenItemGuardado.isPresent()) {
                final Response<ProductoItem> result = new Response(Instant.now(), ordenItemGuardado,
                        HttpStatus.CREATED.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.CREATED);

            } else {
                logger.error("No se pudo ingresar el producto " + productoId + " a la orden.");
                throw new ApiRestException("No se pudo ingresar el producto " + productoId + " a la orden.");
            }
        } catch (EntityNotFoundException nfe) {
            logger.debug(nfe.getMessage());
            throw nfe;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }


    }

    @PatchMapping(value = "orden/{id}/cerrar", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> cerrarOrdenById(@PathVariable(name = "id") final String id)
            throws EntityNotFoundException, ApiRestException {
        logger.info("PATCH orden/{id}/cerrar");

        try {
            Optional<Orden> ordenGuardada = ordenService.cerrarOrdenById(id);

            if (ordenGuardada.isPresent()) {
                final Response<Orden> result = new Response(Instant.now(), ordenGuardada,
                        HttpStatus.OK.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                logger.error("No se pudo cerrar la orden " + id);
                throw new ApiRestException("No se pudo cerrar la orden " + id);
            }
        } catch (EntityNotFoundException nfe) {
            logger.debug(nfe.getMessage());
            throw nfe;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }


    }

    @PatchMapping(value = "orden/{id}/anular", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> anularOrdenById(@PathVariable(name = "id") final String id)
            throws EntityNotFoundException, ApiRestException {
        logger.info("PATCH orden/{id}/anular");

        try {
            Optional<Orden> ordenGuardada = ordenService.cerrarOrdenById(id);

            if (ordenGuardada.isPresent()) {
                final Response<Orden> result = new Response(Instant.now(), ordenGuardada,
                        HttpStatus.OK.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                logger.error("No se pudo anular la orden " + id);
                throw new ApiRestException("No se pudo anular la orden " + id);
            }
        } catch (EntityNotFoundException nfe) {
            logger.debug(nfe.getMessage());
            throw nfe;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }


    }

    @DeleteMapping(value = "orden/{id}/producto/{productoId}", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteItemOrden(@PathVariable(name = "id") final String id,
                                             @PathVariable(name = "productoId") final String productoId,
                                             @Valid @RequestBody final ProductoItemRequest ordenItemRequest)
            throws EntityNotFoundException, ApiRestException {
        logger.info("DELETE /api/orden/{id}/producto/{productoId}");
        logger.debug(ordenItemRequest);
        try {
            Optional<ProductoItem> ordenItemGuardado = ordenService.eliminarProducto(id,
                    productoId, ordenItemRequest.getCantidad());

            if (ordenItemGuardado.isPresent()) {
                final Response<ProductoItem> result = new Response(Instant.now(), ordenItemGuardado,
                        HttpStatus.OK.value(), "Success");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                logger.error("No se pudo eliminar el producto " + productoId + " a la orden.");
                throw new ApiRestException("No se pudo eliminar el producto " + productoId + " de la orden.");
            }
        } catch (EntityNotFoundException nfe) {
            logger.debug(nfe.getMessage());
            throw nfe;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRestException(e.getMessage());
        }

    }

}
