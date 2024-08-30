package com.example.projetomusica.exceptions;

public class BandaAlreadyExistsException extends RuntimeException {
    public BandaAlreadyExistsException(String message) {
        super(message);
    }
}
