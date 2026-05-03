package br.com.clubedossargentos.exception;

public class NaoAutenticadoException extends RuntimeException {
    public NaoAutenticadoException(String message) {
        super(message);
    }
}
