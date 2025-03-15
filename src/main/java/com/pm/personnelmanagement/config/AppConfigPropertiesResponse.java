package com.pm.personnelmanagement.config;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AppConfigPropertiesResponse(@NotNull List<@NotNull AppConfigPropertyDTO> appConfigProperties) {
}
