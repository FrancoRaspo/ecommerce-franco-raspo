package com.ecommerce.francoraspo.handlers;

import com.ecommerce.francoraspo.handlers.exceptions.ApiRestException;
import com.ecommerce.francoraspo.models.responses.Response;
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

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<Response> indexOutOfBoundsException(IndexOutOfBoundsException e) {
        final Response<String> result = new Response(Instant.now(),
                "[PortafolioController] - IndexOutOfBoundsException- Not Found. " + e.getMessage(), 404, "Error");
        return new ResponseEntity<>(result, HttpStatus.IM_USED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response> runtimeException(RuntimeException e) {
        final Response<String> result = new Response(Instant.now(), "[RuntimeException Response] - Exception: Error desconocido " + e.getMessage(), 501, "Error");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ApiRestException.class)
    public ResponseEntity<Response> messageErrorHandle(final ApiRestException e) {
        final Response<String> result = new Response(
                Instant.now(),
                "[ApiRestException Response] - ApiRestException: " + e.getMessage(),
                404,
                "No encontrado");

        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //TODO Con mapping
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
        return errors;
    }
}
