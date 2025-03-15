package com.pm.personnelmanagement.user.dto;

import com.pm.personnelmanagement.user.model.Sex;

import java.time.LocalDate;
import java.util.UUID;

public record UserEditionRequestBody(
        Boolean isActive,
        UUID avatarUUID,
        String role,
        Sex sex,
        LocalDate birthday,
        String username,
        String password,
        String firstName,
        String middleName,
        String lastName,
        String pesel,
        UUID addressUUID,
        UUID additionalAddressUUID,
        String email,
        String phone
) {
}
