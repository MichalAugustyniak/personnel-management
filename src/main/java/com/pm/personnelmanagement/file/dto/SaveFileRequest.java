package com.pm.personnelmanagement.file.dto;

import org.springframework.http.MediaType;

import java.io.InputStream;
import java.util.UUID;

public record SaveFileRequest(
        String filename,
        MediaType mediaType,
        InputStream inputStream,
        UUID userUUID
) {
}
