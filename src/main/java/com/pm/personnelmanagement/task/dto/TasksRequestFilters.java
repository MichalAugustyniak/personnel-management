package com.pm.personnelmanagement.task.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TasksRequestFilters(
        Integer pageNumber,
        Integer pageSize,
        String nameLike,
        String createdBy,
        String user,
        UUID taskEventUUID,
        LocalDateTime from,
        LocalDateTime to
) {
}
