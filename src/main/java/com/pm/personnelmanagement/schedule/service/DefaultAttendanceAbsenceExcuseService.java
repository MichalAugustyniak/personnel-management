package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.common.NullChecker;
import com.pm.personnelmanagement.schedule.dto.AttachAbsenceExcuseToAttendancesDTO;
import com.pm.personnelmanagement.schedule.dto.AttachAttendanceToAbsenceExcusesDTO;
import com.pm.personnelmanagement.schedule.dto.DetachAbsenceExcuseFromAttendancesDTO;
import com.pm.personnelmanagement.schedule.dto.DetachAttendanceFromAbsenceExcusesDTO;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import com.pm.personnelmanagement.schedule.model.Attendance;
import com.pm.personnelmanagement.schedule.repository.AbsenceExcuseRepository;
import com.pm.personnelmanagement.schedule.repository.AttendanceRepository;
import com.pm.personnelmanagement.schedule.utils.AbsenceExcuseUtils;
import com.pm.personnelmanagement.schedule.utils.AttendanceUtils;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class DefaultAttendanceAbsenceExcuseService implements AttendanceAbsenceExcuseService {
    private final AttendanceRepository attendanceRepository;
    private final AbsenceExcuseRepository absenceExcuseRepository;
    private final AttendanceUtils attendanceUtils;
    private final AbsenceExcuseUtils absenceExcuseUtils;

    public DefaultAttendanceAbsenceExcuseService(AttendanceRepository attendanceRepository, AbsenceExcuseRepository absenceExcuseRepository, AttendanceUtils attendanceUtils, AbsenceExcuseUtils absenceExcuseUtils) {
        this.attendanceRepository = attendanceRepository;
        this.absenceExcuseRepository = absenceExcuseRepository;
        this.attendanceUtils = attendanceUtils;
        this.absenceExcuseUtils = absenceExcuseUtils;
    }

    @Override
    public void attachAbsenceExcuseToAttendances(AttachAbsenceExcuseToAttendancesDTO dto) {
        validateDtoAndUUID(dto.absenceExcuseUUID(), dto.attendanceUUIDs(), "Field dto.absenceExcuseUUID", "Argument dto.attendanceUUIDs");

        AbsenceExcuse absenceExcuse = absenceExcuseUtils.fetchAbsenceExcuse(dto.absenceExcuseUUID());
        Set<Attendance> attendances = attendanceUtils.fetchAndValidateAttendances(dto.attendanceUUIDs());

        attendances.forEach(attendance -> attendance.getAbsenceExcuses().add(absenceExcuse));
        attendanceRepository.saveAll(attendances);
    }

    @Override
    public void detachAbsenceExcuseFromAttendances(DetachAbsenceExcuseFromAttendancesDTO dto) {
        validateDtoAndUUID(dto.absenceExcuseUUID(), dto.attendanceUUIDs(), "Field dto.absenceExcuseUUID", "Argument dto.attendanceUUIDs");

        AbsenceExcuse absenceExcuse = absenceExcuseUtils.fetchAbsenceExcuse(dto.absenceExcuseUUID());
        Set<Attendance> attendances = attendanceUtils.fetchAndValidateAttendances(dto.attendanceUUIDs());

        attendances.forEach(attendance -> attendance.getAbsenceExcuses().remove(absenceExcuse));
        attendanceRepository.saveAll(attendances);
    }

    @Override
    public void attachAttendanceToAbsenceExcuses(AttachAttendanceToAbsenceExcusesDTO dto) {
        validateDtoAndUUID(dto.attendanceUUID(), dto.absenceExcuseUUIDs(), "Argument dto.attendanceUUID", "Field dto.absenceExcuseUUIDs");

        Attendance attendance = attendanceUtils.fetchAttendance(dto.attendanceUUID());
        Set<AbsenceExcuse> absenceExcuses = absenceExcuseUtils.fetchAndValidateAbsenceExcuses(dto.absenceExcuseUUIDs());

        absenceExcuses.forEach(absenceExcuse -> absenceExcuse.getAttendances().add(attendance));
        absenceExcuseRepository.saveAll(absenceExcuses);
    }

    @Override
    public void detachAttendanceFromAbsenceExcuses(DetachAttendanceFromAbsenceExcusesDTO dto) {
        validateDtoAndUUID(dto.attendanceUUID(), dto.absenceExcuseUUIDs(), "Argument dto.attendanceUUID", "Field dto.absenceExcuseUUIDs");

        Attendance attendance = attendanceUtils.fetchAttendance(dto.attendanceUUID());
        Set<AbsenceExcuse> absenceExcuses = absenceExcuseUtils.fetchAndValidateAbsenceExcuses(dto.absenceExcuseUUIDs());

        absenceExcuses.forEach(absenceExcuse -> absenceExcuse.getAttendances().remove(attendance));
        absenceExcuseRepository.saveAll(absenceExcuses);
    }

    private void validateDtoAndUUID(UUID absenceExcuseUUID, Set<UUID> attendanceUUIDs, String absenceExcuseMessage, String attendanceMessage) {
        NullChecker.validate(absenceExcuseUUID, absenceExcuseMessage);
        NullChecker.validate(attendanceUUIDs, attendanceMessage);
        attendanceUUIDs.forEach(uuid -> NullChecker.validate(uuid, "Member of attendanceUUIDs set"));
    }
}
