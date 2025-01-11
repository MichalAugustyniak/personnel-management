package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record DetailedAbsenceExcuseStatusUpdateBodyRequest(
        UUID absenceExcuseStatusUUID,
        String message
) {
}
