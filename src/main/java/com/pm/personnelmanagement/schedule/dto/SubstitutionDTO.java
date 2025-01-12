package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SubstitutionDTO(
        String reason,
        LocalDateTime createdAt,
        UUID uuid,
        UUID scheduleDayUUID,
        UUID substitutedUserUUID,
        UUID substitutingUserUUID
) {
}
