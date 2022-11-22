package com.ecommerce.francoraspo.handlers;

import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.handlers.exceptions.EntityNotFoundException;
import com.ecommerce.francoraspo.handlers.exceptions.NoAuthorizedException;
import com.ecommerce.francoraspo.models.responses.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> runtimeException(RuntimeException e) {
        logger.error(e.getMessage());
        final Response<String> result = new Response(Instant.now(), "[RuntimeException Response] - Exception: Error desconocido " + e.getMessage(), 501, "Error");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiRestException.class)
    public ResponseEntity<Response> messageErrorApiRestException(final ApiRestException e) {
        logger.error(e.getMessage());
        final Response<String> result = new Response(
                Instant.now(),
                "ApiRestException: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error");

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NoAuthorizedException.class)
    public ResponseEntity<Response> messageErrorNoAuthorizedException(final NoAuthorizedException e) {
        logger.error(e.getMessage());
        final Response<String> result = new Response(
                Instant.now(),
                "NoAuthorizedException: " + e.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                "Acceso denegado");

        return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response> messageErrorEntityNotFoundException(final EntityNotFoundException e) {
        logger.error(e.getMessage());
        final Response<String> result = new Response(
                Instant.now(),
                "EntityNotFoundException: " + e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "No encontrado");

        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            final MethodArgumentNotValidException ex) {

        final Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        logger.debug(errors.toString());
        return errors;
    }
}
