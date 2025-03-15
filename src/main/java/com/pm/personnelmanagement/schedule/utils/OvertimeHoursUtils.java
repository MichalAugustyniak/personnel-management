package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.OvertimeHoursNotFoundException;
import com.pm.personnelmanagement.schedule.model.OvertimeHours;
import com.pm.personnelmanagement.schedule.repository.OvertimeHoursRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OvertimeHoursUtils {
    private final OvertimeHoursRepository overtimeHoursRepository;

    public OvertimeHoursUtils(OvertimeHoursRepository overtimeHoursRepository) {
        this.overtimeHoursRepository = overtimeHoursRepository;
    }

    public OvertimeHours fetchOvertimeHours(@NotNull UUID uuid) {
        return overtimeHoursRepository.findByUuid(uuid)
                .orElseThrow(() -> new OvertimeHoursNotFoundException(
                        String.format("Overtime hours of uuid %s not found", uuid)
                ));
    }

    public OvertimeHours fetchFullOvertimeHours(@NotNull UUID uuid) {
        return overtimeHoursRepository.findByUuidWithUserAndApprovedByAndSchedule(uuid)
                .orElseThrow(() -> new OvertimeHoursNotFoundException(
                        String.format("Overtime hours of uuid %s not found", uuid)
                ));
    }
}
