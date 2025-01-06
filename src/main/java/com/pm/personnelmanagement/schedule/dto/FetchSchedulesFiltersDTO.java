package com.pm.personnelmanagement.schedule.dto;

import java.util.Optional;
import java.util.UUID;

public record FetchSchedulesFiltersDTO(
        Optional<UUID> userUUID,
        Optional<Boolean> isActive,
        Optional<Integer> pageSize,
        Optional<Integer> pageNumber
) {
}
