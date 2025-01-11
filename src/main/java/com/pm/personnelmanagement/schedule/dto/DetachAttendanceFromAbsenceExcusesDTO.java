package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record DetachAttendanceFromAbsenceExcusesDTO(
        UUID attendanceUUID,
        Set<UUID> absenceExcuseUUIDs
) {
}
