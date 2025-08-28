package com.nthuy.demo_erm.common.exception;

public class BadRequestValidationException extends RuntimeException {
    public BadRequestValidationException(String message) {
        super(message);
    }
}
