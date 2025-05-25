package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

public interface ShiftTypeService {
    ShiftTypeDTO getShiftType(ShiftTypeRequest dto);

    ShiftTypesDTO getShiftTypes(FetchShiftTypesFiltersDTO dto);

    ShiftTypeCreationResponse createShiftType(ShiftTypeCreationRequest dto);

    void updateShiftType(ShiftTypeUpdateRequest dto);

    void deleteShiftType(ShiftTypeDeletionRequest dto);
}
