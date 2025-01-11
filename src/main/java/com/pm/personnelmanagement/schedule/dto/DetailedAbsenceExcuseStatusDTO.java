package com.pm.personnelmanagement.schedule.dto;

import java.util.UUID;

public record DetailedAbsenceExcuseStatusDTO(
       UUID uuid,
       String message,
       boolean isChecked,
       UUID absenceExcuseStatusUUID
) {
}
