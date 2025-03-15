package com.pm.personnelmanagement.schedule.dto;

import java.util.List;

public record SchedulesResponse(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        List<ScheduleMetaDTO> content
) {
}
