package br.com.clubedossargentos.exception;

public class PessoaNaoEncontradaException extends RuntimeException {
    public PessoaNaoEncontradaException(String message) {
        super(message);
    }
}