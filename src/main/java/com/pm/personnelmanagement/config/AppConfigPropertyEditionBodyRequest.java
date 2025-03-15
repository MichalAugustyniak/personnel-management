package com.pm.personnelmanagement.config;

import jakarta.validation.constraints.NotEmpty;

public record AppConfigPropertyEditionBodyRequest(@NotEmpty String propertyValue) {
}
