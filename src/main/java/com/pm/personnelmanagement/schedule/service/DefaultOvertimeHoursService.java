package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.mapper.OvertimeHoursMapper;
import com.pm.personnelmanagement.schedule.model.OvertimeHours;
import com.pm.personnelmanagement.schedule.model.Schedule;
import com.pm.personnelmanagement.schedule.model.ScheduleDay;
import com.pm.personnelmanagement.schedule.repository.OvertimeHoursRepository;
import com.pm.personnelmanagement.schedule.utils.OvertimeHoursUtils;
import com.pm.personnelmanagement.schedule.utils.ScheduleDayUtils;
import com.pm.personnelmanagement.schedule.utils.ScheduleUtils;
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
public class DefaultOvertimeHoursService implements OvertimeHoursService {
    private final OvertimeHoursRepository overtimeHoursRepository;
    private final OvertimeHoursUtils overtimeHoursUtils;
    private final ScheduleDayUtils scheduleDayUtils;
    private final UserUtils userUtils;
    private final ScheduleUtils scheduleUtils;

    public DefaultOvertimeHoursService(OvertimeHoursRepository overtimeHoursRepository, OvertimeHoursUtils overtimeHoursUtils, ScheduleDayUtils scheduleDayUtils, UserUtils userUtils, ScheduleUtils scheduleUtils) {
        this.overtimeHoursRepository = overtimeHoursRepository;
        this.overtimeHoursUtils = overtimeHoursUtils;
        this.scheduleDayUtils = scheduleDayUtils;
        this.userUtils = userUtils;
        this.scheduleUtils = scheduleUtils;
    }

    @Override
    public OvertimeHoursDTO getOvertimeHours(@NotNull @Valid OvertimeHoursRequest dto) {
        return OvertimeHoursMapper.map(overtimeHoursUtils.fetchOvertimeHours(dto.uuid()));
    }

    @Override
    public OvertimeHoursPagedDTO getOvertimeHoursPaged(@NotNull @Valid OvertimeHoursRequestFilters dto) {
        int pageNumber = Optional.ofNullable(dto.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(dto.pageSize()).orElse(10);
        Specification<OvertimeHours> specification = Specification.where(null);
        Optional.ofNullable(dto.approvedByUUID()).ifPresent(uuid -> {
            Specification<OvertimeHours> hasApprovedByUUID = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("approvedBy").get("uuid"), uuid);
            specification.and(hasApprovedByUUID);
        });
        Optional.ofNullable(dto.userUUID()).ifPresent(uuid -> {
            Specification<OvertimeHours> hasUserUUID = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("user").get("uuid"), uuid);
            specification.and(hasUserUUID);
        });
        Optional.ofNullable(dto.scheduleUUID()).ifPresent(uuid -> {
            Specification<OvertimeHours> hasScheduleUUID = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("schedule").get("uuid"), uuid);
            specification.and(hasScheduleUUID);
        });
        Optional.ofNullable(dto.startDateTime()).ifPresent(startDateTime -> {
            Specification<OvertimeHours> hasStartDateTime = (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), startDateTime);
            specification.and(hasStartDateTime);
        });
        Optional.ofNullable(dto.startDateTime()).ifPresent(endDateTime -> {
            Specification<OvertimeHours> hasEndDateTime = (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("endDateTime"), endDateTime);
            specification.and(hasEndDateTime);
        });
        Page<OvertimeHours> overtimeHours = overtimeHoursRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return OvertimeHoursMapper.map(overtimeHours);
    }

    @Override
    public OvertimeHoursCreationResponse createOvertimeHours(@NotNull @Valid OvertimeHoursCreationRequest dto) {
        //ScheduleDay scheduleDay = scheduleDayUtils.fetchScheduleDay(dto.scheduleUUID());
        Schedule schedule = scheduleUtils.fetchSchedule(dto.scheduleUUID());
        User approvedBy = userUtils.fetchUser(dto.approvedByUUID());
        User user = userUtils.fetchUser(dto.userUUID());
        UUID uuid = UUID.randomUUID();
        OvertimeHours overtimeHours = new OvertimeHours();
        overtimeHours.setStartDateTime(dto.startDateTime());
        overtimeHours.setEndDateTime(dto.endDateTime());
        overtimeHours.setUuid(uuid);
        overtimeHours.setCreatedAt(LocalDateTime.now());
        overtimeHours.setApprovedBy(approvedBy);
        overtimeHours.setUser(user);
        //overtimeHours.setScheduleDay(scheduleDay);
        overtimeHours.setSchedule(schedule);
        overtimeHours.setDescription(dto.description());
        overtimeHours.setCompleted(false);
        overtimeHoursRepository.save(overtimeHours);
        return new OvertimeHoursCreationResponse(uuid);
    }

    @Override
    public void updateOvertimeHours(@NotNull @Valid OvertimeHoursUpdateRequest dto) {
        OvertimeHours overtimeHours = overtimeHoursUtils.fetchOvertimeHours(dto.uuid());
        Optional.ofNullable(dto.body().startDateTime()).ifPresent(overtimeHours::setStartDateTime);
        Optional.ofNullable(dto.body().endDateTime()).ifPresent(overtimeHours::setEndDateTime);
        Optional.ofNullable(dto.body().userUUID()).ifPresent(uuid -> {
            User user = userUtils.fetchUser(uuid);
            overtimeHours.setUser(user);
        });
        Optional.ofNullable(dto.body().scheduleUUID()).ifPresent(uuid -> {
            //ScheduleDay scheduleDay = scheduleDayUtils.fetchScheduleDay(uuid);
            Schedule schedule = scheduleUtils.fetchSchedule(uuid);
            overtimeHours.setSchedule(schedule);
        });
        Optional.ofNullable(dto.body().description()).ifPresent(overtimeHours::setDescription);
        Optional.ofNullable(dto.body().isCompleted()).ifPresent(overtimeHours::setCompleted);
        overtimeHoursRepository.save(overtimeHours);
    }

    @Override
    public void deleteOvertimeHours(@NotNull @Valid OvertimeHoursDeletionRequest dto) {
        OvertimeHours overtimeHours = overtimeHoursUtils.fetchOvertimeHours(dto.uuid());
        overtimeHoursRepository.delete(overtimeHours);
    }
}
