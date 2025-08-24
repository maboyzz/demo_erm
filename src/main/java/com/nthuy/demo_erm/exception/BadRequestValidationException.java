package com.nthuy.demo_erm.exception;

public class BadRequestValidationException extends RuntimeException {
    public BadRequestValidationException(String message) {
        super(message);
    }
}
