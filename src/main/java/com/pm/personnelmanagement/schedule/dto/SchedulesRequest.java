package com.pm.personnelmanagement.schedule.dto;

public record SchedulesRequest(
        String user,
        Boolean isActive,
        Integer pageSize,
        Integer pageNumber
) {
}
