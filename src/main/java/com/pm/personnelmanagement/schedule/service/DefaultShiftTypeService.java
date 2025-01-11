package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.CannotDeleteShiftType;
import com.pm.personnelmanagement.schedule.mapper.ShiftTypeMapper;
import com.pm.personnelmanagement.schedule.model.ShiftType;
import com.pm.personnelmanagement.schedule.repository.ShiftTypeRepository;
import com.pm.personnelmanagement.schedule.utils.ShiftTypeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultShiftTypeService implements ShiftTypeService {
    private final ShiftTypeRepository shiftTypeRepository;
    private final ShiftTypeUtils shiftTypeUtils;

    public DefaultShiftTypeService(ShiftTypeRepository shiftTypeRepository, ShiftTypeUtils shiftTypeUtils) {
        this.shiftTypeRepository = shiftTypeRepository;
        this.shiftTypeUtils = shiftTypeUtils;
    }

    @Override
    public ShiftTypeDTO getShiftType(@NotNull @Valid ShiftTypeRequest dto) {
        ShiftType shiftType = shiftTypeUtils.fetchShiftType(dto.uuid());
        return ShiftTypeMapper.map(shiftType);
    }

    @Override
    public ShiftTypesDTO getShiftTypes(@NotNull FetchShiftTypesFiltersDTO dto) {
        int pageNumber = Optional.ofNullable(dto.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(dto.pageSize()).orElse(10);
        Page<ShiftType> shiftTypes = shiftTypeRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return ShiftTypeMapper.map(shiftTypes);
    }

    @Override
    public ShiftTypeCreationResponse createShiftType(@NotNull @Valid ShiftTypeCreationRequest dto) {
        UUID uuid = UUID.randomUUID();
        ShiftType shiftType = new ShiftType();
        shiftType.setUuid(uuid);
        shiftType.setName(dto.name());
        shiftType.setDescription(dto.description());
        shiftType.setStartTime(dto.startTime());
        shiftType.setEndTime(dto.endTime());
        shiftTypeRepository.save(shiftType);
        return new ShiftTypeCreationResponse(uuid);
    }

    @Override
    public void updateShiftType(@NotNull @Valid ShiftTypeUpdateRequest dto) {
        ShiftType shiftType = shiftTypeUtils.fetchShiftType(dto.uuid());
        Optional.ofNullable(dto.body().name()).ifPresent(shiftType::setName);
        Optional.ofNullable(dto.body().description()).ifPresent(shiftType::setDescription);
        Optional.ofNullable(dto.body().startTime()).ifPresent(shiftType::setStartTime);
        Optional.ofNullable(dto.body().endTime()).ifPresent(shiftType::setEndTime);
        shiftTypeRepository.save(shiftType);
    }

    @Override
    public void deleteFilter(@NotNull @Valid ShiftTypeDeletionRequest dto) {
        ShiftType shiftType = shiftTypeUtils.fetchShiftType(dto.uuid());
        if (!shiftType.getScheduleDays().isEmpty()) {
            throw new CannotDeleteShiftType("Cannot delete: in use");
        }
        shiftTypeRepository.save(shiftType);
    }
}
