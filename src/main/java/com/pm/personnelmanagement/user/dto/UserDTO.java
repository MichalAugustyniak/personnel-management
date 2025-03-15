package com.pm.personnelmanagement.user.dto;

import com.pm.personnelmanagement.user.model.Sex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserDTO(
        UUID uuid,
        Boolean isActive,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        String role,
        Sex sex,
        LocalDate birthday,
        String username,
        String firstName,
        String middleName,
        String lastName,
        UUID addressUUID,
        UUID additionalAddressUUID,
        String email,
        String phone
) {
}
