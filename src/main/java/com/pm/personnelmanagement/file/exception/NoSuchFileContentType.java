package com.pm.personnelmanagement.file.exception;

public class NoSuchFileContentType extends RuntimeException {
    public NoSuchFileContentType() {
    }

    public NoSuchFileContentType(String message) {
        super(message);
    }
}
