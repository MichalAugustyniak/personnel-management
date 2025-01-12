package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record SubstitutionUpdateRequestBody(
        String reason,
        UUID scheduleDayUUID,
        UUID substitutedUserUUID,
        UUID substitutingUserUUID
) {
}
