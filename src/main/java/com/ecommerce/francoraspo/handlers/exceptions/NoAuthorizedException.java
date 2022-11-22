package com.ecommerce.francoraspo.handlers.exceptions;

public class NoAuthorizedException extends Exception {
    public NoAuthorizedException(final String message) {
        super(message);
    }
}
