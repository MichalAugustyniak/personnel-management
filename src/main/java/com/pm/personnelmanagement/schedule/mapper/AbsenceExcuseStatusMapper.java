package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.AbsenceExcuseStatusDTO;
import com.pm.personnelmanagement.schedule.dto.AbsenceExcuseStatusesResponse;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuseStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class AbsenceExcuseStatusMapper {
    public static AbsenceExcuseStatusDTO map(@NotNull AbsenceExcuseStatus absenceExcuseStatus) {
        return new AbsenceExcuseStatusDTO(
                absenceExcuseStatus.getUuid(),
                absenceExcuseStatus.getName(),
                absenceExcuseStatus.getDescription()
        );
    }

    public static AbsenceExcuseStatusesResponse map(@NotNull Page<AbsenceExcuseStatus> absenceExcuseStatuses) {
        return new AbsenceExcuseStatusesResponse(
                absenceExcuseStatuses.getTotalElements(),
                absenceExcuseStatuses.getTotalPages(),
                absenceExcuseStatuses.getNumber(),
                absenceExcuseStatuses.getNumberOfElements(),
                absenceExcuseStatuses.getSize(),
                absenceExcuseStatuses.getContent().stream().map(
                absenceExcuseStatus -> new AbsenceExcuseStatusDTO(
                        absenceExcuseStatus.getUuid(),
                        absenceExcuseStatus.getName(),
                        absenceExcuseStatus.getDescription()
                )
        ).collect(Collectors.toSet()));
    }
}
