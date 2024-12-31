package com.pm.personnelmanagement.file.exception;

public class MissingFileMediaTypeException extends RuntimeException {
    public MissingFileMediaTypeException() {
    }

    public MissingFileMediaTypeException(String message) {
        super(message);
    }
}
