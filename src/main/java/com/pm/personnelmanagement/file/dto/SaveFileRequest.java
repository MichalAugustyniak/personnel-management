package com.pm.personnelmanagement.file.dto;

import com.pm.personnelmanagement.file.model.FileContentType;

import java.io.InputStream;
import java.util.UUID;

public record SaveFileRequest(
        String filename,
        FileContentType contentType,
        InputStream inputStream,
        UUID userUUID
) {
}
