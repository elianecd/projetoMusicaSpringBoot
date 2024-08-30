package com.example.projetomusica.exceptions;

public class AlbumAlreadyExistsException extends RuntimeException {
    public AlbumAlreadyExistsException(String message) {
        super(message);
    }
}
