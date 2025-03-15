package com.pm.personnelmanagement.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class DefaultAppConfigPropertyService {
    private final AppConfigPropertyRepository appConfigPropertyRepository;
    private final AppConfigPropertyUtils appConfigPropertyUtils;

    public DefaultAppConfigPropertyService(AppConfigPropertyRepository appConfigPropertyRepository, AppConfigPropertyUtils appConfigPropertyUtils) {
        this.appConfigPropertyRepository = appConfigPropertyRepository;
        this.appConfigPropertyUtils = appConfigPropertyUtils;
    }

    public AppConfigPropertyDTO getAppConfigProperty(@NotNull @Valid AppConfigPropertyRequest request) {
        AppConfigProperty property = appConfigPropertyUtils.fetchAppConfigPropertyByName(request.name());
        return new AppConfigPropertyDTO(property.getPropertyName(), property.getPropertyValue());
    }

    public AppConfigPropertiesResponse getAppConfigProperties(@NotNull @Valid AppConfigPropertiesRequest request) {
        return new AppConfigPropertiesResponse(appConfigPropertyRepository.findAll().stream().map(appConfigProperty -> new AppConfigPropertyDTO(
                appConfigProperty.getPropertyName(),
                appConfigProperty.getPropertyValue()
        )).toList());
    }

    public void createAppConfigProperty(@NotNull @Valid AppConfigPropertyCreationRequest request) {
        AppConfigProperty property = new AppConfigProperty();
        property.setPropertyName(request.propertyName());
        property.setPropertyValue(request.propertyValue());
        appConfigPropertyRepository.save(property);
    }

    public void updateAppConfigProperty(@NotNull @Valid AppConfigPropertyEditionRequest request) {
        AppConfigProperty property = appConfigPropertyUtils.fetchAppConfigPropertyByName(request.propertyName());
        property.setPropertyValue(request.request().propertyValue());
        appConfigPropertyRepository.save(property);
    }
}
