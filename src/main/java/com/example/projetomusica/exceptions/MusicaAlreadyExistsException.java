package com.example.projetomusica.exceptions;

public class MusicaAlreadyExistsException extends RuntimeException{
    public MusicaAlreadyExistsException(String message) {
        super(message);
    }
}
