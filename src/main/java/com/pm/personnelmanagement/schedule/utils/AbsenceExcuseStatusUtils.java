package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.AbsenceExcuseNotFoundException;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuseStatus;
import com.pm.personnelmanagement.schedule.repository.AbsenceExcuseStatusRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AbsenceExcuseStatusUtils {
    private AbsenceExcuseStatusRepository absenceExcuseStatusRepository;

    public AbsenceExcuseStatusUtils(AbsenceExcuseStatusRepository absenceExcuseStatusRepository) {
        this.absenceExcuseStatusRepository = absenceExcuseStatusRepository;
    }

    public AbsenceExcuseStatus fetchAbsenceExcuseStatus(@NotNull UUID uuid) {
        return absenceExcuseStatusRepository.findByUuid(uuid).orElseThrow(
                () -> new AbsenceExcuseNotFoundException(
                        String.format("Absence excuse status of uuid %s not found", uuid)
                )
        );
    }
}
