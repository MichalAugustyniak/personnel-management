package com.pm.personnelmanagement.schedule.dto;

import java.util.List;

public record AttendanceListDTO(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        List<AttendanceDTO> attendances
) {
}
