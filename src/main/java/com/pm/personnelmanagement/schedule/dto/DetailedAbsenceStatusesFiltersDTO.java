package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record DetailedAbsenceStatusesFiltersDTO(
        Integer pageNumber,
        Integer pageSize,
        Boolean isChecked,
        UUID absenceExcuseStatusUUID
) {
}
