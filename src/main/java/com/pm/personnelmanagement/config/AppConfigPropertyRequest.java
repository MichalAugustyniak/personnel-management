package com.pm.personnelmanagement.config;

import jakarta.validation.constraints.NotEmpty;

public record AppConfigPropertyRequest(@NotEmpty String name) {
}
