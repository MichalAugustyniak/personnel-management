package com.pm.personnelmanagement.file.controller;

import com.pm.personnelmanagement.file.dto.SaveFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileController {
    ResponseEntity<SaveFileResponse> save(
            MultipartFile file,
            UUID userUUID
    );

    ResponseEntity<Void> deleteByUUID(UUID uuid);

    ResponseEntity<Resource> findByUUID(UUID uuid);

    ResponseEntity<Page<Resource>> findAll();
}
