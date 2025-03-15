package com.pm.personnelmanagement.config;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
public class AppConfigPropertyUtils {
    private final AppConfigPropertyRepository appConfigPropertyRepository;

    public AppConfigPropertyUtils(AppConfigPropertyRepository appConfigPropertyRepository) {
        this.appConfigPropertyRepository = appConfigPropertyRepository;
    }

    public AppConfigProperty fetchAppConfigPropertyById(long id) {
        return appConfigPropertyRepository.findById(id)
                .orElseThrow(() -> new AppConfigPropertyNotFoundException(
                        String.format("App config property of id %s not found", id)
                ));
    }

    public AppConfigProperty fetchAppConfigPropertyByName(@NotEmpty String propertyName) {
        return appConfigPropertyRepository.findByPropertyName(propertyName)
                .orElseThrow(() -> new AppConfigPropertyNotFoundException(
                        String.format("App config property of name %s not found", propertyName)
                ));
    }
}
