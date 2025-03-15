package com.pm.personnelmanagement.config;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record AppConfigPropertyCreationRequest(
        @NotEmpty @Length(min = 3) String propertyName,
        @NotEmpty @Length(min = 3) String propertyValue
) {
}
