package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.DetailedAbsenceExcuseStatusNotFoundException;
import com.pm.personnelmanagement.schedule.model.DetailedAbsenceExcuseStatus;
import com.pm.personnelmanagement.schedule.repository.DetailedAbsenceExcuseStatusRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DetailedAbsenceExcuseStatusUtils {
    private final DetailedAbsenceExcuseStatusRepository detailedAbsenceExcuseStatusRepository;

    public DetailedAbsenceExcuseStatusUtils(DetailedAbsenceExcuseStatusRepository detailedAbsenceExcuseStatusRepository) {
        this.detailedAbsenceExcuseStatusRepository = detailedAbsenceExcuseStatusRepository;
    }

    public DetailedAbsenceExcuseStatus fetchDetailedAbsenceExcuseStatus(@NotNull UUID uuid) {
        return detailedAbsenceExcuseStatusRepository.findByUuid(uuid).orElseThrow(
                () -> new DetailedAbsenceExcuseStatusNotFoundException(
                        String.format("Detailed absence excuse status of uuid %s not found", uuid)
                )
        );
    }
}
