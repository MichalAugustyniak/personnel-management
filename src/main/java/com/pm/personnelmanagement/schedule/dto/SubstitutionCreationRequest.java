package com.pm.personnelmanagement.schedule.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubstitutionCreationRequest(
        String reason,
        @NotNull UUID scheduleDayUUID,
        @NotNull UUID substitutedUserUUID,
        @NotNull UUID substitutingUserUUID
) {
}
