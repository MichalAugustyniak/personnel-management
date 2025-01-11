package com.pm.personnelmanagement.schedule.controller;

import com.pm.personnelmanagement.schedule.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ShiftTypeController {
    ResponseEntity<ShiftTypeDTO> getShiftType(UUID uuid);

    ResponseEntity<ShiftTypesDTO> getShiftTypes(Integer pageNumber, Integer pageSize);

    ResponseEntity<ShiftTypeCreationResponse> createShiftType(ShiftTypeCreationRequest dto);

    ResponseEntity<Void> updateShiftType(UUID uuid, ShiftTypeUpdateRequestBody dto);

    ResponseEntity<Void> deleteFilter(UUID uuid);
}
