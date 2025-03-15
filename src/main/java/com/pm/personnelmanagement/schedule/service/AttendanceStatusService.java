package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

public interface AttendanceStatusService {
    AttendanceStatusResponse getAttendanceStatus(AttendanceStatusRequest dto);

    AttendanceStatusesResponse getAttendanceStatuses(FetchAttendanceStatusesFiltersDTO dto);

    AttendanceStatusCreationResponse createAttendanceStatus(AttendanceStatusCreationRequest dto);

    void updateAttendanceStatus(UpdateAttendanceStatusDTO dto);

    void deleteAttendanceStatus(DeleteAttendanceStatusDTO dto);
}
