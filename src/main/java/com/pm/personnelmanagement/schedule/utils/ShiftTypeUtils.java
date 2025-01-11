package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.ShiftTypeNotFoundException;
import com.pm.personnelmanagement.schedule.model.ShiftType;
import com.pm.personnelmanagement.schedule.repository.ShiftTypeRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShiftTypeUtils {
    private final ShiftTypeRepository shiftTypeRepository;

    public ShiftTypeUtils(ShiftTypeRepository shiftTypeRepository) {
        this.shiftTypeRepository = shiftTypeRepository;
    }

    public ShiftType fetchShiftType(@NotNull UUID uuid) {
        return shiftTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ShiftTypeNotFoundException(
                        String.format("Shift type of uuid %s not found", uuid)
                )
        );
    }
}
