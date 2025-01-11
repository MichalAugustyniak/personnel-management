package com.pm.personnelmanagement.schedule.dto;

import java.time.LocalTime;

public record ShiftTypeUpdateRequestBody(
        String name,
        String description,
        LocalTime startTime,
        LocalTime endTime
) {
}
