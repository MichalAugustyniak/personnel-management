package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record FetchAttendancesFiltersDTO(
        Integer pageNumber,
        Integer pageSize,
        UUID userUUID,
        UUID scheduleDayUUID,
        UUID absenceExcuseUUID,
        UUID attendanceStatusUUID
) {
}
