package com.pm.personnelmanagement.file.model;

public enum FileMetaData {

    FILE_UUID("fileUUID"),
    USER_UUID("userUUID"),
    CONTENT_TYPE("_contentType");

    private final String value;

    FileMetaData(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
