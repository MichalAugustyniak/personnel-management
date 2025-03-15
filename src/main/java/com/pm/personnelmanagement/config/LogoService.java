package com.pm.personnelmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LogoService {
    private final AppConfigPropertyRepository appConfigPropertyRepository;
    private final AppConfigPropertyUtils appConfigPropertyUtils;

    public LogoService(AppConfigPropertyRepository appConfigPropertyRepository, AppConfigPropertyUtils appConfigPropertyUtils) {
        this.appConfigPropertyRepository = appConfigPropertyRepository;
        this.appConfigPropertyUtils = appConfigPropertyUtils;
    }

    @Value("${app.logo.upload-dir}")
    private String uploadDir;

    public String uploadLogo(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty!");
        }

        String filename = "logo-" + System.currentTimeMillis() + ".png";
        Path filePath = Paths.get(uploadDir, filename);

        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        AppConfigProperty property = appConfigPropertyUtils.fetchAppConfigPropertyByName(AppConfigProperties.LOGO_PATH);
        property.setPropertyValue("/uploads/" + filename);
        appConfigPropertyRepository.save(property);

        return property.getPropertyValue();
    }
}
