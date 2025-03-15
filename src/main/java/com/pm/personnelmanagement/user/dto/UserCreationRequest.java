package com.pm.personnelmanagement.user.dto;

import com.pm.personnelmanagement.user.model.Sex;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

public record UserCreationRequest(
        String username,
        @Length(min = 4, max = 15) @NotEmpty String password,
        boolean isActive,
        @NotEmpty String role,
        @NotNull Sex sex,
        @NotNull LocalDate birthday,
        @NotEmpty String firstName,
        String middleName,
        @NotEmpty String lastName,
        @NotNull UUID addressUUID,
        UUID additionalAddressUUID,
        String email,
        String phone
        ) {
}
