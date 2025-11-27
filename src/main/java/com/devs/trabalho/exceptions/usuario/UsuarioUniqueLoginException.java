package com.devs.trabalho.exceptions.usuario;

public class UsuarioUniqueLoginException extends RuntimeException {
    public UsuarioUniqueLoginException(String message) {
        super(message);
    }
}
