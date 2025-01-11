package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record AttachAttendanceToAbsenceExcusesDTO(
        UUID attendanceUUID,
        Set<UUID> absenceExcuseUUIDs
) {
}
