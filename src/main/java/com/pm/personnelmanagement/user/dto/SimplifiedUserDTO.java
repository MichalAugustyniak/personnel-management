package com.pm.personnelmanagement.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SimplifiedUserDTO(
        UUID uuid,
        String username,
        LocalDateTime lastLoginAt,
        String firstName,
        String middleName,
        String lastName
) {
}
