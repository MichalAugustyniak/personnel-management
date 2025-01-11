package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;
import java.util.UUID;

public record DetachAbsenceExcuseFromAttendancesDTO(
        UUID absenceExcuseUUID,
        Set<UUID> attendanceUUIDs
) {
}
