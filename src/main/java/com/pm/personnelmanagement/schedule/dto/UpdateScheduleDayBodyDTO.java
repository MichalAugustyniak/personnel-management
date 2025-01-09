package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UpdateScheduleDayBodyDTO(
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        UUID shiftTypeUUID,
        Set<UpdateWorkBreakDTO> existingWorkBreaks,
        Set<CreateWorkBreakDTO> newWorkBreaks,
        Set<UUID> deletedWorkBreaks
) {
}
