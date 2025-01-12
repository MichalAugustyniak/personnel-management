package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

public interface OvertimeHoursService {
    OvertimeHoursDTO getOvertimeHours(OvertimeHoursRequest dto);

    OvertimeHoursPagedDTO getOvertimeHoursPaged(OvertimeHoursRequestFilters dto);

    OvertimeHoursCreationResponse createOvertimeHours(OvertimeHoursCreationRequest dto);

    void updateOvertimeHours(OvertimeHoursUpdateRequest dto);

    void deleteOvertimeHours(OvertimeHoursDeletionRequest dto);
}
