package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.AttachAbsenceExcuseToAttendancesDTO;
import com.pm.personnelmanagement.schedule.dto.AttachAttendanceToAbsenceExcusesDTO;
import com.pm.personnelmanagement.schedule.dto.DetachAbsenceExcuseFromAttendancesDTO;
import com.pm.personnelmanagement.schedule.dto.DetachAttendanceFromAbsenceExcusesDTO;

public interface AttendanceAbsenceExcuseService {
    void attachAbsenceExcuseToAttendances(AttachAbsenceExcuseToAttendancesDTO dto);

    void detachAbsenceExcuseFromAttendances(DetachAbsenceExcuseFromAttendancesDTO dto);

    void attachAttendanceToAbsenceExcuses(AttachAttendanceToAbsenceExcusesDTO dto);

    void detachAttendanceFromAbsenceExcuses(DetachAttendanceFromAbsenceExcusesDTO dto);
}
