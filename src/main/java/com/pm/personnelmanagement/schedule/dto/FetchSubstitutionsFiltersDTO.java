package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FetchSubstitutionsFiltersDTO(
        Integer pageNumber,
        Integer pageSize,
        UUID scheduleDayUUID,
        UUID substitutedUserUUID,
        UUID substitutingUserUUID,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}
