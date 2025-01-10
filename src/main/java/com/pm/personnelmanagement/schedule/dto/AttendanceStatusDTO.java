package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record AttendanceStatusDTO(
        UUID uuid,
        String name,
        String description
) {
}
