package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record AttendanceStatusResponse(
        UUID uuid,
        String name,
        String description,
        boolean isExcusable
) {
}
