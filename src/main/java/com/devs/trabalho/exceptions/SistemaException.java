package com.devs.trabalho.exceptions;

public class SistemaException extends RuntimeException {
    public SistemaException(String message, Exception cause) {
        super(message, cause);
    }
}
