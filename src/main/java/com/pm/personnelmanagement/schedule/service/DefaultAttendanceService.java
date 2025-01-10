package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.AbsenceExcuseNotFoundException;
import com.pm.personnelmanagement.schedule.exception.AttendanceNotFoundException;
import com.pm.personnelmanagement.schedule.exception.AttendanceStatusNotFoundException;
import com.pm.personnelmanagement.schedule.exception.ScheduleDayNotFoundException;
import com.pm.personnelmanagement.schedule.mapper.AttendanceMapper;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import com.pm.personnelmanagement.schedule.model.Attendance;
import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import com.pm.personnelmanagement.schedule.model.ScheduleDay;
import com.pm.personnelmanagement.schedule.repository.AbsenceExcuseRepository;
import com.pm.personnelmanagement.schedule.repository.AttendanceRepository;
import com.pm.personnelmanagement.schedule.repository.AttendanceStatusRepository;
import com.pm.personnelmanagement.schedule.repository.ScheduleDayRepository;
import com.pm.personnelmanagement.user.exception.UserNotFoundException;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultAttendanceService implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final AbsenceExcuseRepository absenceExcuseRepository;
    private final AttendanceStatusRepository attendanceStatusRepository;
    private final ScheduleDayRepository scheduleDayRepository;
    private final UserRepository userRepository;

    public DefaultAttendanceService(AttendanceRepository attendanceRepository, AbsenceExcuseRepository absenceExcuseRepository, AttendanceStatusRepository attendanceStatusRepository, ScheduleDayRepository scheduleDayRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.absenceExcuseRepository = absenceExcuseRepository;
        this.attendanceStatusRepository = attendanceStatusRepository;
        this.scheduleDayRepository = scheduleDayRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AttendanceDTO getAttendance(@NotNull UUID uuid) {
        Optional.ofNullable(uuid).orElseThrow(() -> new IllegalArgumentException("Argument UUID cannot be null"));
        Attendance attendance = attendanceRepository.findByUuid(uuid).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance of uuid %s not found", uuid))
        );
        return AttendanceMapper.map(attendance);
    }

    @Override
    public AttendanceListDTO getAttendances(@NotNull FetchAttendancesFiltersDTO filters) {
        Optional.ofNullable(filters).orElseThrow(() -> new IllegalArgumentException("Argument filters cannot be null"));
        Specification<Attendance> specification = Specification.where(null);
        Optional.ofNullable(filters.absenceExcuseUUID()).ifPresent(uuid -> {
            AbsenceExcuse absenceExcuse = absenceExcuseRepository.findByUuid(uuid).orElseThrow(
                    () -> new AbsenceExcuseNotFoundException(String.format("Absence excuse of uuid %s not found", uuid))
            );
            Specification<Attendance> hasAbsenceExcuse = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("absenceExcuses"), absenceExcuse);
            specification.and(hasAbsenceExcuse);
        });
        Optional.ofNullable(filters.attendanceStatusUUID()).ifPresent(uuid -> {
            AttendanceStatus attendanceStatus = attendanceStatusRepository.findByUuid(uuid).orElseThrow(
                    () -> new AttendanceStatusNotFoundException(String.format("Attendance status of uuid %s not found", uuid))
            );
            Specification<Attendance> hasAttendanceStatus = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("attendanceStatus"), attendanceStatus);
            specification.and(hasAttendanceStatus);
        });
        Optional.ofNullable(filters.scheduleDayUUID()).ifPresent(uuid -> {
            ScheduleDay scheduleDay = scheduleDayRepository.findByUuid(uuid).orElseThrow(
                    () -> new ScheduleDayNotFoundException(String.format("Schedule day of uuid %s not found", uuid))
            );
            Specification<Attendance> hasScheduleDay = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("scheduleDay"), scheduleDay);
            specification.and(hasScheduleDay);
        });
        Optional.ofNullable(filters.userUUID()).ifPresent(uuid -> {
            User user = userRepository.findByUuid(uuid).orElseThrow(
                    () -> new UserNotFoundException(String.format("User of uuid %s not found", uuid))
            );
            Specification<Attendance> hasUser = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("user"), user);
            specification.and(hasUser);
        });
        int pageNumber = Optional.ofNullable(filters.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(filters.pageSize()).orElse(10);
        Page<Attendance> attendances = attendanceRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return AttendanceMapper.map(attendances);
    }

    @Override
    public UUID createAttendance(@NotNull CreateAttendanceDTO dto) {
        Optional.ofNullable(dto).orElseThrow(() -> new IllegalArgumentException("Argument dto cannot be null"));
        UUID userUUID = Optional.ofNullable(dto.userUUID()).orElseThrow(() -> new IllegalArgumentException("Field userUUID cannot be null"));
        UUID attendanceStatusUUID = Optional.ofNullable(dto.attendanceStatusUUID()).orElseThrow(() -> new IllegalArgumentException("Field attendanceStatusUUID cannot be null"));
        UUID scheduleDayUUID = Optional.ofNullable(dto.scheduleUUID()).orElseThrow(
                () -> new IllegalArgumentException("Field scheduleDayUUID cannot be null")
        );
        LocalDateTime startDateTime = Optional.ofNullable(dto.startDateTime()).orElseThrow(() -> new IllegalArgumentException("Field startDateTime cannot be null"));
        LocalDateTime endDateTime = Optional.ofNullable(dto.endDateTime()).orElseThrow(() -> new IllegalArgumentException("Field endDateTime cannot be null"));
        User user = userRepository.findByUuid(userUUID).orElseThrow(() -> new UserNotFoundException(String.format("User of uuid %s not found", userUUID)));
        AttendanceStatus attendanceStatus = attendanceStatusRepository.findByUuid(attendanceStatusUUID).orElseThrow(
                () -> new AttendanceStatusNotFoundException(String.format("Attendance status of uuid %s not found", attendanceStatusUUID))
        );
        ScheduleDay scheduleDay = scheduleDayRepository.findByUuid(scheduleDayUUID).orElseThrow(
                () -> new ScheduleDayNotFoundException(String.format("Schedule day of uuid %s not found", dto.scheduleUUID()))
        );
        UUID uuid = UUID.randomUUID();
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setAttendanceStatus(attendanceStatus);
        attendance.setStartDateTime(startDateTime);
        attendance.setEndDateTime(endDateTime);
        attendance.setUuid(uuid);
        attendance.setScheduleDay(scheduleDay);
        attendanceRepository.save(attendance);
        return uuid;
    }

    @Override
    public void updateAttendance(@NotNull UpdateAttendanceDTO dto) {
        Optional.ofNullable(dto).orElseThrow(() -> new IllegalArgumentException("Argument dto cannot be null"));
        UUID attendanceUUID = Optional.ofNullable(dto.uuid()).orElseThrow(() -> new IllegalArgumentException("Field uuid cannot be null"));
        UUID attendanceStatusUUID = Optional.ofNullable(dto.updateAttendanceBody().attendanceStatus()).orElseThrow(() -> new IllegalArgumentException("Field attendanceStatus cannot be null"));
        LocalDateTime startDateTime = Optional.ofNullable(dto.updateAttendanceBody().startDateTime()).orElseThrow(() -> new IllegalArgumentException("Field startDateTime cannot be null"));
        LocalDateTime endDateTime = Optional.ofNullable(dto.updateAttendanceBody().endDateTime()).orElseThrow(() -> new IllegalArgumentException("Field endDateTime cannot be null"));

        Attendance attendance = attendanceRepository.findByUuid(attendanceUUID).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance of uuid %s not found", attendanceUUID))
        );
        AttendanceStatus attendanceStatus = attendanceStatusRepository.findByUuid(attendanceStatusUUID).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance status of uuid %s not found", attendanceStatusUUID))
        );
        attendance.setAttendanceStatus(attendanceStatus);
        attendance.setStartDateTime(startDateTime);
        attendance.setEndDateTime(endDateTime);
        attendanceRepository.save(attendance);
    }

    @Override
    public void deleteAttendance(@NotNull UUID uuid) {
        UUID attendanceUUID = Optional.ofNullable(uuid).orElseThrow(() -> new IllegalArgumentException("Argument uuid cannot be null"));
        Attendance attendance = attendanceRepository.findByUuid(attendanceUUID).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance of uuid %s not found", attendanceUUID))
        );
        attendanceRepository.delete(attendance);
    }
}
