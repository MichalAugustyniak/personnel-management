package com.pm.personnelmanagement.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class DefaultAppConfigPropertyController {
    private final DefaultAppConfigPropertyService appConfigPropertyService;
    private final InitialDataService initialDataService;
    private final LogoService logoService;

    public DefaultAppConfigPropertyController(DefaultAppConfigPropertyService appConfigPropertyService, InitialDataService initialDataService, LogoService logoService) {
        this.appConfigPropertyService = appConfigPropertyService;
        this.initialDataService = initialDataService;
        this.logoService = logoService;
    }

    @GetMapping("/{propertyName}")
    public ResponseEntity<AppConfigPropertyDTO> getAppConfigProperty(@NotEmpty @PathVariable String propertyName) {
        return ResponseEntity.ok(appConfigPropertyService.getAppConfigProperty(new AppConfigPropertyRequest(propertyName)));
    }

    @GetMapping
    public ResponseEntity<AppConfigPropertiesResponse> getAppConfigProperties() {
        return ResponseEntity.ok(appConfigPropertyService.getAppConfigProperties(new AppConfigPropertiesRequest()));
    }

    @PostMapping
    public ResponseEntity<Void> createAppConfigProperty(@NotNull @Valid @RequestBody AppConfigPropertyCreationRequest request) {
        appConfigPropertyService.createAppConfigProperty(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{propertyName}")
    public ResponseEntity<Void> patchAppConfigProperty(@NotEmpty @PathVariable String propertyName, @NotNull @Valid @RequestBody AppConfigPropertyEditionBodyRequest body) {
        appConfigPropertyService.updateAppConfigProperty(new AppConfigPropertyEditionRequest(propertyName, body));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/init")
    public ResponseEntity<Void> init() {
        this.initialDataService.insertInitialData();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/upload-logo")
    public ResponseEntity<?> uploadLogo(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = logoService.uploadLogo(file);
            return ResponseEntity.ok(Map.of("logoPath", filePath));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("File save error");
        }
    }

    @GetMapping("/logo")
    public ResponseEntity<AppConfigPropertyDTO> getLogoPath() {
        return ResponseEntity.ok(appConfigPropertyService.getAppConfigProperty(new AppConfigPropertyRequest(AppConfigProperties.LOGO_PATH)));
    }
}
