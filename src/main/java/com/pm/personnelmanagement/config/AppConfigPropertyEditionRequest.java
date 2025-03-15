package com.pm.personnelmanagement.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AppConfigPropertyEditionRequest(@NotEmpty String propertyName, @NotNull AppConfigPropertyEditionBodyRequest request) {
}
