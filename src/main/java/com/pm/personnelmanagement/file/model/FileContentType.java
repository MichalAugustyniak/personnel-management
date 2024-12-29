package com.pm.personnelmanagement.file.model;

import com.pm.personnelmanagement.file.exception.NoSuchFileContentType;

public enum FileContentType {
    IMAGE_PNG("image/png"),
    IMAGE_JPEG("image/jpeg");

    private final String value;

    FileContentType(String value) {
        this.value = value;
    }

    public static FileContentType of(String contentType) {
        return switch (contentType) {
            case "image/png" -> FileContentType.IMAGE_PNG;
            case "image/jpeg" -> FileContentType.IMAGE_JPEG;
            default -> throw new NoSuchFileContentType(String.format("No such file content type %s", contentType));
        };
    }

    public String value() {
        return value;
    }
}
