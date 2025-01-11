package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record FetchAbsenceExcusesFiltersDTO(
     Integer pageNumber,
     Integer pageSize,
     UUID attendanceUUID
) {
}
