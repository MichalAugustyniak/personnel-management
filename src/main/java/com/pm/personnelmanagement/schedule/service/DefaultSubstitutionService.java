package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.mapper.SubstitutionMapper;
import com.pm.personnelmanagement.schedule.model.ScheduleDay;
import com.pm.personnelmanagement.schedule.model.Substitution;
import com.pm.personnelmanagement.schedule.repository.SubstitutionRepository;
import com.pm.personnelmanagement.schedule.utils.ScheduleDayUtils;
import com.pm.personnelmanagement.schedule.utils.SubstitutionUtils;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.UserUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultSubstitutionService implements SubstitutionService {
    private final SubstitutionUtils substitutionUtils;
    private final SubstitutionRepository substitutionRepository;
    private final ScheduleDayUtils scheduleDayUtils;
    private final UserUtils userUtils;

    public DefaultSubstitutionService(SubstitutionUtils substitutionUtils, SubstitutionRepository substitutionRepository, ScheduleDayUtils scheduleDayUtils, UserUtils userUtils) {
        this.substitutionUtils = substitutionUtils;
        this.substitutionRepository = substitutionRepository;
        this.scheduleDayUtils = scheduleDayUtils;
        this.userUtils = userUtils;
    }

    @Override
    public SubstitutionDTO getSubstitution(@NotNull @Valid SubstitutionRequest dto) {
        Substitution substitution = substitutionUtils.fetchSubstitution(dto.uuid());
        return SubstitutionMapper.map(substitution);
    }

    @Override
    public SubstitutionsDTO getSubstitutions(@NotNull @Valid FetchSubstitutionsFiltersDTO dto) {
        int pageNumber = Optional.ofNullable(dto.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(dto.pageSize()).orElse(10);
        Specification<Substitution> specification = Specification.where(null);
        Optional.ofNullable(dto.endDateTime()).ifPresent(endDateTime -> {
            Specification<Substitution> hasEndDateTime = (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDateTime);
            specification.and(hasEndDateTime);
        });
        Optional.ofNullable(dto.startDateTime()).ifPresent(startDateTime -> {
            Specification<Substitution> hasStartDateTime = (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDateTime);
            specification.and(hasStartDateTime);
        });
        Optional.ofNullable(dto.substitutedUserUUID()).ifPresent(uuid -> {
            Specification<Substitution> hasSubstitutedUser = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("substitutedUser").get("uuid"), uuid);
            specification.and(hasSubstitutedUser);
        });
        Optional.ofNullable(dto.substitutingUserUUID()).ifPresent(uuid -> {
            Specification<Substitution> hasSubstitutingUser = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("substitutingUser").get("uuid"), uuid);
            specification.and(hasSubstitutingUser);
        });
        Optional.ofNullable(dto.scheduleDayUUID()).ifPresent(scheduleDay -> {
            Specification<Substitution> hasScheduleDay = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("scheduleDay").get("uuid"), scheduleDay);
            specification.and(hasScheduleDay);
        });
        Page<Substitution> substitutions = substitutionRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return SubstitutionMapper.map(substitutions);
    }

    @Override
    public SubstitutionCreationResponse createSubstitution(@NotNull @Valid SubstitutionCreationRequest dto) {
        ScheduleDay scheduleDay = scheduleDayUtils.fetchScheduleDay(dto.scheduleDayUUID());
        User substitutedUser = userUtils.fetchUser(dto.substitutedUserUUID());
        User substitutingUser = userUtils.fetchUser(dto.substitutingUserUUID());
        UUID uuid = UUID.randomUUID();
        Substitution substitution = new Substitution();
        Optional.ofNullable(dto.reason()).ifPresent(substitution::setReason);
        substitution.setCreatedAt(LocalDateTime.now());
        substitution.setReason(dto.reason());
        substitution.setSubstitutedUser(substitutedUser);
        substitution.setSubstitutingUser(substitutingUser);
        substitution.setScheduleDay(scheduleDay);
        substitution.setUuid(uuid);
        return new SubstitutionCreationResponse(uuid);
    }

    @Override
    public void updateSubstitution(@NotNull @Valid SubstitutionUpdateRequest dto) {
        Substitution substitution = substitutionUtils.fetchSubstitution(dto.uuid());
        Optional.ofNullable(dto.body().reason()).ifPresent(substitution::setReason);
        Optional.ofNullable(dto.body().substitutedUserUUID()).ifPresent(uuid -> {
            User substitutedUser = userUtils.fetchUser(uuid);
            substitution.setSubstitutedUser(substitutedUser);
        });
        Optional.ofNullable(dto.body().substitutingUserUUID()).ifPresent(uuid -> {
            User substitutingUser = userUtils.fetchUser(uuid);
            substitution.setSubstitutedUser(substitutingUser);
        });
        Optional.ofNullable(dto.body().scheduleDayUUID()).ifPresent(uuid -> {
            ScheduleDay scheduleDay = scheduleDayUtils.fetchScheduleDay(uuid);
            substitution.setScheduleDay(scheduleDay);
        });
        Optional.ofNullable(dto.body().reason()).ifPresent(substitution::setReason);
        substitutionRepository.save(substitution);
    }

    @Override
    public void deleteSubstitution(SubstitutionDeletionRequest dto) {
        Substitution substitution = substitutionUtils.fetchSubstitution(dto.uuid());
        substitutionRepository.delete(substitution);
    }
}
