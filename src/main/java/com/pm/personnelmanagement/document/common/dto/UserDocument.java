package com.pm.personnelmanagement.document.common.dto;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record UserDocument(Resource resource, MediaType mediaType, String filename) {
}
