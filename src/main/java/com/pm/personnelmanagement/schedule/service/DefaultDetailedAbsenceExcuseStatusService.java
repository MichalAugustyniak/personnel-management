package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.mapper.DetailedAbsenceExcuseStatusMapper;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuse;
import com.pm.personnelmanagement.schedule.model.AbsenceExcuseStatus;
import com.pm.personnelmanagement.schedule.model.DetailedAbsenceExcuseStatus;
import com.pm.personnelmanagement.schedule.repository.DetailedAbsenceExcuseStatusRepository;
import com.pm.personnelmanagement.schedule.utils.AbsenceExcuseStatusUtils;
import com.pm.personnelmanagement.schedule.utils.AbsenceExcuseUtils;
import com.pm.personnelmanagement.schedule.utils.DetailedAbsenceExcuseStatusUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultDetailedAbsenceExcuseStatusService implements DetailedAbsenceExcuseStatusService {
    private final DetailedAbsenceExcuseStatusUtils detailedAbsenceExcuseStatusUtils;
    private final DetailedAbsenceExcuseStatusRepository detailedAbsenceExcuseStatusRepository;
    private final AbsenceExcuseUtils absenceExcuseUtils;
    private final AbsenceExcuseStatusUtils absenceExcuseStatusUtils;

    public DefaultDetailedAbsenceExcuseStatusService(DetailedAbsenceExcuseStatusUtils detailedAbsenceExcuseStatusUtils, DetailedAbsenceExcuseStatusRepository detailedAbsenceExcuseStatusRepository, AbsenceExcuseUtils absenceExcuseUtils, AbsenceExcuseStatusUtils absenceExcuseStatusUtils) {
        this.detailedAbsenceExcuseStatusUtils = detailedAbsenceExcuseStatusUtils;
        this.detailedAbsenceExcuseStatusRepository = detailedAbsenceExcuseStatusRepository;
        this.absenceExcuseUtils = absenceExcuseUtils;
        this.absenceExcuseStatusUtils = absenceExcuseStatusUtils;
    }

    @Override
    public DetailedAbsenceExcuseStatusDTO getDetailedAbsenceExcuseStatus(@NotNull @Valid DetailedAbsenceExcuseStatusRequest dto) {
        DetailedAbsenceExcuseStatus detailedAbsenceExcuseStatus = detailedAbsenceExcuseStatusUtils.fetchDetailedAbsenceExcuseStatus(
                dto.uuid()
        );
        return DetailedAbsenceExcuseStatusMapper.map(detailedAbsenceExcuseStatus);
    }

    @Override
    public DetailedAbsenceExcuseStatusesDTO getDetailedAbsenceExcuseStatuses(@NotNull @Valid DetailedAbsenceStatusesFiltersDTO dto) {
        int pageNumber = Optional.ofNullable(dto.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(dto.pageSize()).orElse(10);
        Specification<DetailedAbsenceExcuseStatus> specification = Specification.where(null);
        Optional.ofNullable(dto.absenceExcuseStatusUUID()).ifPresent(uuid -> {
            Specification<DetailedAbsenceExcuseStatus> hasAEStatus = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("absenceExcuseStatus").get("uuid"), uuid);
            specification.and(hasAEStatus);
        });
        Optional.ofNullable(dto.isChecked()).ifPresent(isChecked -> {
            Specification<DetailedAbsenceExcuseStatus> hasIsChecked = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isChecked"), isChecked);
            specification.and(hasIsChecked);
        });
        Page<DetailedAbsenceExcuseStatus> detailedAbsenceExcuseStatuses = detailedAbsenceExcuseStatusRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return DetailedAbsenceExcuseStatusMapper.map(detailedAbsenceExcuseStatuses);
    }

    @Override
    public DetailedAbsenceExcusesStatusCreationResponse createDetailedAbsenceExcuseStatus(@NotNull @Valid DetailedAbsenceExcuseStatusCreationRequest dto) {
        AbsenceExcuse absenceExcuse = absenceExcuseUtils.fetchAbsenceExcuse(dto.absenceExcuseUUID());
        AbsenceExcuseStatus absenceExcuseStatus = absenceExcuseStatusUtils.fetchAbsenceExcuseStatus(dto.absenceExcuseStatusUUID());
        UUID uuid = UUID.randomUUID();
        DetailedAbsenceExcuseStatus detailedAbsenceExcuseStatus = new DetailedAbsenceExcuseStatus();
        detailedAbsenceExcuseStatus.setAbsenceExcuse(absenceExcuse);
        detailedAbsenceExcuseStatus.setAbsenceExcuseStatus(absenceExcuseStatus);
        detailedAbsenceExcuseStatus.setUuid(uuid);
        detailedAbsenceExcuseStatus.setChecked(true);
        detailedAbsenceExcuseStatus.setMessage(detailedAbsenceExcuseStatus.getMessage());
        detailedAbsenceExcuseStatusRepository.save(detailedAbsenceExcuseStatus);
        return new DetailedAbsenceExcusesStatusCreationResponse(uuid);
    }

    @Override
    public void updateDetailedAbsenceExcuseStatus(@NotNull @Valid DetailedAbsenceExcuseStatusUpdateRequest dto) {
        DetailedAbsenceExcuseStatus detailedAbsenceExcuseStatus = detailedAbsenceExcuseStatusUtils.fetchDetailedAbsenceExcuseStatus(dto.uuid());
        Optional.ofNullable(dto.body().absenceExcuseStatusUUID()).ifPresent(uuid -> {
            AbsenceExcuseStatus absenceExcuseStatus = absenceExcuseStatusUtils.fetchAbsenceExcuseStatus(dto.body().absenceExcuseStatusUUID());
            detailedAbsenceExcuseStatus.setAbsenceExcuseStatus(absenceExcuseStatus);
        });
        Optional.ofNullable(dto.body().message()).ifPresent(detailedAbsenceExcuseStatus::setMessage);
        detailedAbsenceExcuseStatusRepository.save(detailedAbsenceExcuseStatus);
    }

    @Override
    public void deleteDetailedAbsenceExcuseStatus(@NotNull @Valid DetailedAbsenceExcuseStatusDeleteRequest dto) {
        DetailedAbsenceExcuseStatus detailedAbsenceExcuseStatus = detailedAbsenceExcuseStatusUtils.fetchDetailedAbsenceExcuseStatus(dto.uuid());
        detailedAbsenceExcuseStatusRepository.delete(detailedAbsenceExcuseStatus);
    }
}
