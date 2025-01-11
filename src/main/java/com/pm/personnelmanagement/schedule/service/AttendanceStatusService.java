package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

public interface AttendanceStatusService {
    AttendanceStatusDTO getAttendanceStatus(AttendanceStatusRequest dto);

    AttendanceStatusesDTO getAttendanceStatuses(FetchAttendanceStatusesFiltersDTO dto);

    CreateAttendanceStatusResponse createAttendanceStatus(CreateAttendanceStatusDTO dto);

    void updateAttendanceStatus(UpdateAttendanceStatusDTO dto);

    void deleteAttendanceStatus(DeleteAttendanceStatusDTO dto);
}
