package com.pm.personnelmanagement.file.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
