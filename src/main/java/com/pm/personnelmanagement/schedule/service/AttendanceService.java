package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;

import java.util.UUID;

public interface AttendanceService {
    AttendanceDTO getAttendance(UUID uuid);
    AttendanceListDTO getAttendances(FetchAttendancesFiltersDTO filters);
    UUID createAttendance(CreateAttendanceDTO dto);
    void updateAttendance(UpdateAttendanceDTO dto);
    void deleteAttendance(UUID uuid);
}
