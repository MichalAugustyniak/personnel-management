package com.pm.personnelmanagement.file.service;

import com.pm.personnelmanagement.file.dto.SaveFileRequest;
import com.pm.personnelmanagement.file.dto.SaveFileResponse;
import com.pm.personnelmanagement.file.model.File;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface FileService {
    UUID save(SaveFileRequest request);

    void deleteByUUID(UUID uuid);

    File findByUUID(UUID uuid);

    Page<File> findAll();
}
