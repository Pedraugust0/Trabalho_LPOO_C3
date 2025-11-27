package com.devs.trabalho.exceptions.estoque;

public class EstoqueNotFoundException extends RuntimeException {
    public EstoqueNotFoundException(String message) {
        super(message);
    }
}
