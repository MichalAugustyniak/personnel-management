package com.pm.personnelmanagement.file.dto;

import java.util.UUID;

public record SaveFileRepositoryRequest(UUID fileUUID, SaveFileRequest saveFileRequest) {
}
