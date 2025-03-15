package com.pm.personnelmanagement.taskevent.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskEventsRequest(
        String like,
        LocalDateTime from,
        LocalDateTime to,
        String user,
        String createdBy,
        int pageNumber,
        int pageSize
) {
}
