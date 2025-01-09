package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record FetchSchedulesFiltersDTO(
        UUID userUUID,
        Boolean isActive,
        Integer pageSize,
        Integer pageNumber
) {
}
