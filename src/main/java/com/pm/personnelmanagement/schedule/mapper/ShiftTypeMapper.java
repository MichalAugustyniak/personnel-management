package com.pm.personnelmanagement.schedule.mapper;

import com.pm.personnelmanagement.schedule.dto.ShiftTypeDTO;
import com.pm.personnelmanagement.schedule.dto.ShiftTypesDTO;
import com.pm.personnelmanagement.schedule.model.ShiftType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class ShiftTypeMapper {
    public static ShiftTypeDTO map(@NotNull ShiftType shiftType) {
        return new ShiftTypeDTO(
                shiftType.getUuid(),
                shiftType.getName(),
                shiftType.getDescription(),
                shiftType.getStartTime(),
                shiftType.getEndTime()
        );
    }

    public static ShiftTypesDTO map(@NotNull Page<ShiftType> shiftTypes) {
        return new ShiftTypesDTO(
                shiftTypes.getTotalElements(),
                shiftTypes.getTotalPages(),
                shiftTypes.getNumber(),
                shiftTypes.getNumberOfElements(),
                shiftTypes.getSize(),
                shiftTypes.getContent().stream().map(ShiftTypeMapper::map)
                        .collect(Collectors.toSet())
        );
    }
}
