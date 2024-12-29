package com.pm.personnelmanagement.file.exception;

public class NoSuchMetaDataField extends RuntimeException {
    public NoSuchMetaDataField() {
    }

    public NoSuchMetaDataField(String message) {
        super(message);
    }
}
