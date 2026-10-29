package com.curso.estoque2026.application;

public class RecursoEmUsoException extends RuntimeException {

    public RecursoEmUsoException(String mensagem) {
        super(mensagem);
    }
}
