package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record AbsenceExcuseDTO(
        UUID uuid,
        String name,
        String description,
        String fileUUID,
        DetailedAbsenceExcuseStatusDTO detailedAbsenceExcuseStatus
) {
}
