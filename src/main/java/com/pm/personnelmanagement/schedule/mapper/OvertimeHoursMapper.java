package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.OvertimeHoursDTO;
import com.pm.personnelmanagement.schedule.dto.OvertimeHoursPagedDTO;
import com.pm.personnelmanagement.schedule.model.OvertimeHours;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class OvertimeHoursMapper {
    public static OvertimeHoursDTO map(@NotNull OvertimeHours overtimeHours) {
        return new OvertimeHoursDTO(
                overtimeHours.getUuid(),
                overtimeHours.getUser().getUuid(),
                overtimeHours.getApprovedBy().getUuid(),
                overtimeHours.getScheduleDay().getUuid(),
                overtimeHours.getStartDateTime(),
                overtimeHours.getEndDateTime(),
                overtimeHours.getCreatedAt(),
                overtimeHours.getDescription()
        );
    }

    public static OvertimeHoursPagedDTO map(@NotNull Page<OvertimeHours> overtimeHours) {
        return new OvertimeHoursPagedDTO(
                overtimeHours.getTotalElements(),
                overtimeHours.getTotalPages(),
                overtimeHours.getNumber(),
                overtimeHours.getNumberOfElements(),
                overtimeHours.getSize(),
                overtimeHours.getContent().stream().map(OvertimeHoursMapper::map)
                        .collect(Collectors.toSet())
        );
    }
}
