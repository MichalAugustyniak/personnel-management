package com.pm.personnelmanagement.file.repository;

import com.pm.personnelmanagement.file.dto.SaveFileRepositoryRequest;
import com.pm.personnelmanagement.file.model.File;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository {
    void save(SaveFileRepositoryRequest request);

    void deleteByUUID(UUID uuid);

    Optional<File> findByUUID(UUID uuid);

    Page<File> findAll();
}
