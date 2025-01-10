package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record AbsenceExcuseStatusDTO(
        UUID uuid,
        String description,
        String name
) {
}
