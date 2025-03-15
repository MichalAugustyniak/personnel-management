package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;

public interface AttendanceService {
    AttendanceResponse getAttendance(AuthenticatedRequest<AttendanceRequest> request);
    AttendancesResponse getAttendances(AuthenticatedRequest<AttendancesRequest> request);
    AttendanceCreationResponse createAttendance(AttendanceCreationRequest request);
    void updateAttendance(AuthenticatedRequest<AttendanceUpdateRequest> request);
    void deleteAttendance(AuthenticatedRequest<AttendanceDeleteRequest> request);
}
