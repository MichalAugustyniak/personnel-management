package com.pm.personnelmanagement.file.service;

import com.pm.personnelmanagement.file.dto.SaveFileRepositoryRequest;
import com.pm.personnelmanagement.file.dto.SaveFileRequest;
import com.pm.personnelmanagement.file.exception.FileNotFoundException;
import com.pm.personnelmanagement.file.model.File;
import com.pm.personnelmanagement.file.repository.FileRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultFileService implements FileService {
    private final FileRepository fileRepository;

    public DefaultFileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public UUID save(SaveFileRequest request) {
        UUID fileUUID = UUID.randomUUID();
        fileRepository.save(
                new SaveFileRepositoryRequest(
                        fileUUID,
                        request
                )
        );
        return fileUUID;
    }

    @Override
    public void deleteByUUID(UUID uuid) {
        fileRepository.deleteByUUID(uuid);
    }

    @Override
    public File findByUUID(UUID uuid) {
        return fileRepository.findByUUID(uuid)
                .orElseThrow(
                        () -> new FileNotFoundException(
                                String.format("File of UUID %s not found", uuid.toString())
                        )
                );
    }

    @Override
    public Page<File> findAll() {
        return null;
    }
}
