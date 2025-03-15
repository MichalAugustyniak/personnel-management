package com.pm.personnelmanagement.task.dto;

import java.util.List;

public record TasksResponse(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        List<TaskDTO> tasks
) {
}
