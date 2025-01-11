package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.CannotDeleteAbsenceExcuseStatus;
import com.pm.personnelmanagement.schedule.mapper.AbsenceExcuseStatusMapper;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuseStatus;
import com.pm.personnelmanagement.schedule.repository.AbsenceExcuseStatusRepository;
import com.pm.personnelmanagement.schedule.utils.AbsenceExcuseStatusUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultAbsenceExcuseStatusService implements AbsenceExcuseStatusService {
    private final AbsenceExcuseStatusUtils absenceExcuseStatusUtils;
    private final AbsenceExcuseStatusRepository absenceExcuseStatusRepository;

    public DefaultAbsenceExcuseStatusService(AbsenceExcuseStatusUtils absenceExcuseStatusUtils, AbsenceExcuseStatusRepository absenceExcuseStatusRepository) {
        this.absenceExcuseStatusUtils = absenceExcuseStatusUtils;
        this.absenceExcuseStatusRepository = absenceExcuseStatusRepository;
    }

    @Override
    public AbsenceExcuseStatusDTO getAbsenceExcuseStatus(@NotNull UUID uuid) {
        AbsenceExcuseStatus absenceExcuseStatus = absenceExcuseStatusUtils.fetchAbsenceExcuseStatus(uuid);
        return AbsenceExcuseStatusMapper.map(absenceExcuseStatus);
    }

    @Override
    public AbsenceExcuseStatusesResponse getAbsenceExcuses(@NotNull FetchAbsenceExcuseStatusesFiltersDTO filters) {
        int pageNumber = Optional.ofNullable(filters.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(filters.pageSize()).orElse(10);
        Page<AbsenceExcuseStatus> absenceExcuseStatuses = absenceExcuseStatusRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return AbsenceExcuseStatusMapper.map(absenceExcuseStatuses);
    }

    @Override
    public UUID createAbsenceExcuseStatus(@NotNull @Valid CreateAbsenceStatusDTO dto) {
        UUID uuid = UUID.randomUUID();
        AbsenceExcuseStatus absenceExcuseStatus = new AbsenceExcuseStatus();
        absenceExcuseStatus.setUuid(uuid);
        absenceExcuseStatus.setName(dto.name());
        absenceExcuseStatus.setDescription(dto.description());
        absenceExcuseStatusRepository.save(absenceExcuseStatus);
        return uuid;
    }

    @Override
    public void updateAbsenceStatus(@NotNull @Valid UpdateAbsenceStatusDTO dto) {
        AbsenceExcuseStatus absenceExcuseStatus = absenceExcuseStatusUtils.fetchAbsenceExcuseStatus(dto.uuid());
        Optional.ofNullable(dto.body().name()).ifPresent(absenceExcuseStatus::setName);
        Optional.ofNullable(dto.body().description()).ifPresent(absenceExcuseStatus::setDescription);
        absenceExcuseStatusRepository.save(absenceExcuseStatus);
    }

    @Override
    public void deleteAbsenceStatus(@NotNull UUID uuid) {
        AbsenceExcuseStatus absenceExcuseStatus = absenceExcuseStatusUtils.fetchAbsenceExcuseStatus(uuid);
        if (!absenceExcuseStatus.getDetailedAbsenceExcuseStatuses().isEmpty()) {
            throw new CannotDeleteAbsenceExcuseStatus("Cannot delete the absence excuse status: in use");
        }
        absenceExcuseStatusRepository.delete(absenceExcuseStatus);
    }
}
