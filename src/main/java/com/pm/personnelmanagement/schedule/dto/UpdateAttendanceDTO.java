package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record UpdateAttendanceDTO(
        UUID uuid,
        UpdateAttendanceBodyDTO updateAttendanceBody
) {
}
