package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.AbsenceExcuseDTO;
import com.pm.personnelmanagement.schedule.dto.AbsenceExcuseListDTO;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public class AbsenceExcuseMapper {
    public static AbsenceExcuseDTO map(AbsenceExcuse absenceExcuse) {
        Optional.ofNullable(absenceExcuse).orElseThrow(() -> new IllegalArgumentException("Argument absenceExcuse cannot be null"));
        return new AbsenceExcuseDTO(
                absenceExcuse.getUuid(),
                absenceExcuse.getName(),
                absenceExcuse.getDescription(),
                absenceExcuse.getFileUUID(),
                absenceExcuse.getDetailedAbsenceExcuseStatus().getUuid()
        );
    }

    public static AbsenceExcuseListDTO map(@NotNull Page<AbsenceExcuse> absenceExcuses) {
        Optional.ofNullable(absenceExcuses).orElseThrow(() -> new IllegalArgumentException("Argument absenceExcuses cannot be null"));
        return new AbsenceExcuseListDTO(
                absenceExcuses.getTotalElements(),
                absenceExcuses.getTotalPages(),
                absenceExcuses.getNumber(),
                absenceExcuses.getNumberOfElements(),
                absenceExcuses.getSize(),
                absenceExcuses.getContent().stream().map(absenceExcuse -> new AbsenceExcuseDTO(
                        absenceExcuse.getUuid(),
                        absenceExcuse.getName(),
                        absenceExcuse.getDescription(),
                        absenceExcuse.getFileUUID(),
                        absenceExcuse.getDetailedAbsenceExcuseStatus().getUuid()
                )).toList()
        );
    }
}
