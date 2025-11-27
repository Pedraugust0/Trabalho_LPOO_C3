package com.devs.trabalho.exceptions.produto;

public class ProdutoNotInAEstoqueException extends RuntimeException {
    public ProdutoNotInAEstoqueException(String message) {
        super(message);
    }
}
