package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.common.UnauthorizedException;
import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.*;
import com.pm.personnelmanagement.schedule.mapper.AttendanceMapper;
import com.pm.personnelmanagement.schedule.model.*;
import com.pm.personnelmanagement.schedule.repository.*;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.exception.UserNotFoundException;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import com.pm.personnelmanagement.user.util.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import java.util.UUID;

@Validated
@Service
public class DefaultAttendanceService implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final AttendanceStatusRepository attendanceStatusRepository;
    private final ScheduleDayRepository scheduleDayRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;
    private final UserScheduleRepository userScheduleRepository;

    public DefaultAttendanceService(AttendanceRepository attendanceRepository, AttendanceStatusRepository attendanceStatusRepository, ScheduleDayRepository scheduleDayRepository, UserRepository userRepository, UserUtils userUtils, UserScheduleRepository userScheduleRepository) {
        this.attendanceRepository = attendanceRepository;
        this.attendanceStatusRepository = attendanceStatusRepository;
        this.scheduleDayRepository = scheduleDayRepository;
        this.userRepository = userRepository;
        this.userUtils = userUtils;
        this.userScheduleRepository = userScheduleRepository;
    }

    @Override
    public AttendanceResponse getAttendance(AuthenticatedRequest<AttendanceRequest> request) {
        Attendance attendance = attendanceRepository.findByUuid(request.request().uuid()).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance of uuid %s not found", request.request().uuid()))
        );
        return AttendanceMapper.map(attendance);
    }

    @Override
    public AttendancesResponse getAttendances(AuthenticatedRequest<AttendancesRequest> request) {
        System.out.println(request);
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        boolean isEmployee = principalUser.getRole().getName().equals(DefaultRoleNames.EMPLOYEE);
        Specification<Attendance> specification = Specification.where(null);

        if (isEmployee && !principalUser.getUsername().equals(request.request().user()) && request.request().user() != null) {
            throw new UnauthorizedException("You are not allowed to do this");
        }

        if (isEmployee) {
            Specification<Attendance> hasUser = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("user"), principalUser);
            specification = specification.and(hasUser);

        } else if (request.request().user() != null) {
            Specification<Attendance> hasUser = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("user").get("username"), "%" + request.request().user() + "%");
            specification = specification.and(hasUser);
        }

        if (request.request().attendanceStatusUUID() != null) {
            AttendanceStatus attendanceStatus = attendanceStatusRepository.findByUuid(request.request().attendanceStatusUUID()).orElseThrow(
                    () -> new AttendanceStatusNotFoundException(String.format("Attendance status of uuid %s not found", request.request().attendanceStatusUUID()))
            );
            Specification<Attendance> hasAttendanceStatus = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("attendanceStatus"), attendanceStatus);
            specification = specification.and(hasAttendanceStatus);
        }

        if (request.request().scheduleDayUUID() != null) {
            ScheduleDay scheduleDay = scheduleDayRepository.findByUuid(request.request().scheduleDayUUID()).orElseThrow(
                    () -> new ScheduleDayNotFoundException(String.format("Schedule day of uuid %s not found", request.request().scheduleDayUUID()))
            );
            Specification<Attendance> hasScheduleDay = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("scheduleDay"), scheduleDay);
            specification = specification.and(hasScheduleDay);
        }

        int pageNumber = Optional.ofNullable(request.request().pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(request.request().pageSize()).orElse(10);
        Page<Attendance> attendances = attendanceRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return AttendanceMapper.map(attendances);
    }

    @Override
    public AttendanceCreationResponse createAttendance(AttendanceCreationRequest request) {
        User user = userRepository.findByUsername(request.user()).orElseThrow(() -> new UserNotFoundException(String.format("User of username %s not found", request.user())));
        AttendanceStatus attendanceStatus = attendanceStatusRepository.findByUuid(request.attendanceStatusUUID()).orElseThrow(
                () -> new AttendanceStatusNotFoundException(String.format("Attendance status of uuid %s not found", request.attendanceStatusUUID()))
        );
        ScheduleDay scheduleDay = scheduleDayRepository.findByUuid(request.scheduleDayUUID()).orElseThrow(
                () -> new ScheduleDayNotFoundException(String.format("Schedule day of uuid %s not found", request.scheduleDayUUID()))
        );
        UUID uuid = UUID.randomUUID();
        Attendance attendance = new Attendance();
        attendance.setUser(user);
        attendance.setAttendanceStatus(attendanceStatus);
        attendance.setStartDateTime(request.startDateTime());
        attendance.setEndDateTime(request.endDateTime());
        attendance.setUuid(uuid);
        attendance.setScheduleDay(scheduleDay);
        attendanceRepository.save(attendance);
        return new AttendanceCreationResponse(uuid);
    }

    @Override
    public void updateAttendance(AuthenticatedRequest<AttendanceUpdateRequest> request) {
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        boolean isManager = principalUser.getRole().getName().equals(DefaultRoleNames.MANAGER);
        UserSchedule userSchedule = userScheduleRepository.findByIsActiveAndUser(true, principalUser)
                .orElseThrow(() -> new UserScheduleNotFoundException("This user has no active schedule"));
        Attendance attendance = attendanceRepository.findByUuid(request.request().uuid()).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance of uuid %s not found", request.request().uuid()))
        );
        if (isManager && !attendance.getScheduleDay().getSchedule().getUuid().equals(userSchedule.getSchedule().getUuid())) {
            throw new UnauthorizedException("You are not allowed to do that");
        }
        Optional.ofNullable(request.request().updateAttendanceBody().attendanceStatus()).ifPresent(uuid -> {
            AttendanceStatus attendanceStatus = attendanceStatusRepository.findByUuid(uuid).orElseThrow(
                    () -> new AttendanceNotFoundException(String.format("Attendance status of uuid %s not found", uuid))
            );
            attendance.setAttendanceStatus(attendanceStatus);
        });
        Optional.ofNullable(request.request().updateAttendanceBody().startDateTime()).ifPresent(attendance::setStartDateTime);
        Optional.ofNullable(request.request().updateAttendanceBody().endDateTime()).ifPresent(attendance::setEndDateTime);
        attendanceRepository.save(attendance);
    }

    @Override
    public void deleteAttendance(AuthenticatedRequest<AttendanceDeleteRequest> request) {
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        boolean isManager = principalUser.getRole().getName().equals(DefaultRoleNames.MANAGER);
        Attendance attendance = attendanceRepository.findByUuid(request.request().uuid()).orElseThrow(
                () -> new AttendanceNotFoundException(String.format("Attendance of uuid %s not found", request.request().uuid()))
        );
        attendanceRepository.delete(attendance);
    }
}
