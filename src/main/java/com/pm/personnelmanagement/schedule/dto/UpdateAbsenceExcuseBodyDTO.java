package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record UpdateAbsenceExcuseBodyDTO(
        String name,
        String description,
        UUID fileUUID
) {
}
