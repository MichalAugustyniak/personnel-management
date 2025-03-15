package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.CannotDeleteAttendanceStatusException;
import com.pm.personnelmanagement.schedule.mapper.AttendanceStatusMapper;
import com.pm.personnelmanagement.schedule.model.AttendanceStatus;
import com.pm.personnelmanagement.schedule.repository.AttendanceStatusRepository;
import com.pm.personnelmanagement.schedule.utils.AttendanceStatusUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultAttendanceStatusService implements AttendanceStatusService {
    private final AttendanceStatusRepository attendanceStatusRepository;
    private final AttendanceStatusUtils attendanceStatusUtils;

    public DefaultAttendanceStatusService(AttendanceStatusRepository attendanceStatusRepository, AttendanceStatusUtils attendanceStatusUtils) {
        this.attendanceStatusRepository = attendanceStatusRepository;
        this.attendanceStatusUtils = attendanceStatusUtils;
    }

    @Override
    public AttendanceStatusResponse getAttendanceStatus(@NotNull @Valid AttendanceStatusRequest dto) {
        AttendanceStatus attendanceStatus = attendanceStatusUtils.fetchAttendanceStatus(dto.uuid());
        return AttendanceStatusMapper.map(attendanceStatus);
    }

    @Override
    public AttendanceStatusesResponse getAttendanceStatuses(@NotNull @Valid FetchAttendanceStatusesFiltersDTO dto) {
        int pageNumber = Optional.ofNullable(dto.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(dto.pageSize()).orElse(10);
        Page<AttendanceStatus> attendanceStatuses = attendanceStatusRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return AttendanceStatusMapper.map(attendanceStatuses);
    }

    @Override
    public AttendanceStatusCreationResponse createAttendanceStatus(@NotNull @Valid AttendanceStatusCreationRequest dto) {
        UUID uuid = UUID.randomUUID();
        AttendanceStatus attendanceStatus = new AttendanceStatus();
        attendanceStatus.setName(dto.name());
        attendanceStatus.setDescription(dto.description());
        attendanceStatus.setUuid(uuid);
        attendanceStatus.setExcusable(dto.isExcusable());
        attendanceStatusRepository.save(attendanceStatus);
        return new AttendanceStatusCreationResponse(uuid);
    }

    @Override
    public void updateAttendanceStatus(@NotNull @Valid UpdateAttendanceStatusDTO dto) {
        AttendanceStatus attendanceStatus = attendanceStatusUtils.fetchAttendanceStatus(dto.uuid());
        Optional.ofNullable(dto.body().name()).ifPresent(attendanceStatus::setName);
        Optional.ofNullable(dto.body().description()).ifPresent(attendanceStatus::setDescription);
        attendanceStatusRepository.save(attendanceStatus);
    }

    @Override
    public void deleteAttendanceStatus(@NotNull @Valid DeleteAttendanceStatusDTO dto) {
        AttendanceStatus attendanceStatus = attendanceStatusUtils.fetchAttendanceStatus(dto.uuid());
        if (!attendanceStatus.getAttendances().isEmpty()) {
            throw new CannotDeleteAttendanceStatusException("Cannot delete: is use");
        }
        attendanceStatusRepository.delete(attendanceStatus);
    }
}
