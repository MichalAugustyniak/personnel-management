package com.pm.personnelmanagement.schedule.dto;

import java.util.Set;

public record AbsenceExcuseStatusesResponse(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        Set<AbsenceExcuseStatusDTO> absenceExcuses
) {
}
