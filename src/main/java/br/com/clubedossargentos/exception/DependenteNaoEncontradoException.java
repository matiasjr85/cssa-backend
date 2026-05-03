package br.com.clubedossargentos.exception;

public class DependenteNaoEncontradoException extends RuntimeException {
    public DependenteNaoEncontradoException(String message) {
        super(message);
    }
}