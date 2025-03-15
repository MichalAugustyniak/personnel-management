package com.pm.personnelmanagement.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty String username,
        @NotEmpty String password
) {
}
