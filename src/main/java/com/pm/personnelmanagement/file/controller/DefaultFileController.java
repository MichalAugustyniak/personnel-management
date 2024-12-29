package com.pm.personnelmanagement.file.controller;

import com.pm.personnelmanagement.file.dto.SaveFileRequest;
import com.pm.personnelmanagement.file.dto.SaveFileResponse;
import com.pm.personnelmanagement.file.exception.NoSuchFileContentType;
import com.pm.personnelmanagement.file.model.File;
import com.pm.personnelmanagement.file.model.FileContentType;
import com.pm.personnelmanagement.file.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class DefaultFileController implements FileController {
    private final FileService fileService;

    public DefaultFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    @Override
    public ResponseEntity<SaveFileResponse> save(
            @RequestParam MultipartFile file,
            @RequestParam UUID userUUID
    ) {
        System.out.println("Received POST request");
        System.out.println("Invoking save service method...");
        try {
            UUID fileUUID = fileService.save(
                    new SaveFileRequest(
                            file.getOriginalFilename(),
                            FileContentType.of(
                                    Optional.ofNullable(file.getContentType()).orElseThrow(
                                            () -> new NoSuchFileContentType("File content type is empty")
                                    )
                            ),
                            file.getInputStream(),
                            userUUID
                    )
            );
            System.out.println("Returned response");
            return new ResponseEntity<>(
                    new SaveFileResponse(fileUUID.toString()),
                    HttpStatus.CREATED
            );
        } catch (IOException e) {
            System.out.println("IOException occurred");
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{uuid}")
    @Override
    public ResponseEntity<Void> deleteByUUID(@PathVariable UUID uuid) {
        fileService.deleteByUUID(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{uuid}")
    @Override
    public ResponseEntity<Resource> findByUUID(@PathVariable UUID uuid) {
        File file = fileService.findByUUID(uuid);
        System.out.println("Trying to return a response...");
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getFileInfo().getContentType()))
                .body(file.getFileResource().getResource());
    }

    @Override
    public ResponseEntity<Page<Resource>> findAll() {
        return null;
    }
}
