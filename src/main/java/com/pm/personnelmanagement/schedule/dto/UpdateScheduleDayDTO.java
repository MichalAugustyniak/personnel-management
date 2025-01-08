package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record UpdateScheduleDayDTO(
        UUID scheduleDayUUID,
        UpdateScheduleDayBodyDTO scheduleDay
) {
}
