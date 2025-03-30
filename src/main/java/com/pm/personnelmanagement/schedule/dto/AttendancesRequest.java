package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record AttendancesRequest(
        Integer pageNumber,
        Integer pageSize,
        String user,
        UUID scheduleDayUUID,
        UUID attendanceStatusUUID
) {
}
