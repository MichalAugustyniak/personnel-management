package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OvertimeHoursRequestFilters(
        Integer pageNumber,
        Integer pageSize,
        UUID approvedByUUID,
        UUID userUUID,
        //UUID scheduleDayUUID,
        UUID scheduleUUID,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}
