package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.CannotDeleteSchedule;
import com.pm.personnelmanagement.schedule.exception.ScheduleNotFoundException;
import com.pm.personnelmanagement.schedule.exception.ShiftTypeNotFound;
import com.pm.personnelmanagement.schedule.exception.WorkBreakNotFound;
import com.pm.personnelmanagement.schedule.mapper.ScheduleMapper;
import com.pm.personnelmanagement.schedule.model.*;
import com.pm.personnelmanagement.schedule.repository.ScheduleDayRepository;
import com.pm.personnelmanagement.schedule.repository.ScheduleRepository;
import com.pm.personnelmanagement.schedule.repository.ShiftTypeRepository;
import com.pm.personnelmanagement.schedule.repository.UserScheduleRepository;
import com.pm.personnelmanagement.user.exception.UserNotFoundException;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DefaultScheduleService implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;
    private final ShiftTypeRepository shiftTypeRepository;
    private final ScheduleDayRepository scheduleDayRepository;

    public DefaultScheduleService(ScheduleRepository scheduleRepository, UserScheduleRepository userScheduleRepository, UserRepository userRepository, ShiftTypeRepository shiftTypeRepository, ScheduleDayRepository scheduleDayRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userScheduleRepository = userScheduleRepository;
        this.userRepository = userRepository;
        this.shiftTypeRepository = shiftTypeRepository;
        this.scheduleDayRepository = scheduleDayRepository;
    }

    @Override
    public UUID createSchedule(CreateScheduleDTO dto) {
        Optional.ofNullable(dto).orElseThrow(() -> new IllegalArgumentException("Method argument cannot be null"));
        Set<User> users = userRepository.findAllByUuidIn(dto.userUUIDList());
        if (users.size() != dto.userUUIDList().size()) {
            throw new UserNotFoundException("Some of the users might not exist");
        }
        Set<UUID> uuids = new HashSet<>();
        Set<CreateScheduleDayDTO> scheduleDays = Optional.ofNullable(dto.scheduleDays()).orElse(Collections.emptySet());
        scheduleDays.removeIf(Objects::isNull);
        for (var day : scheduleDays) {
            if (Optional.ofNullable(day.shiftTypeUUID()).isEmpty()) {
                continue;
            }
            uuids.add(day.shiftTypeUUID());
        }
        Map<UUID, ShiftType> shiftTypeMap = fetchUUIDsToShiftTypeMap(uuids);
        Schedule schedule = new Schedule();
        schedule.setUuid(UUID.randomUUID());
        schedule.setName(dto.name());
        schedule.setDescription(dto.description());
        schedule.setEnableHolidayAssignments(dto.enableHolidayAssignments());
        schedule.setEnableWorkingSaturdays(dto.enableWorkingSaturdays());
        schedule.setEnableWorkingSundays(dto.enableWorkingSundays());
        schedule.setMaxWorkingHoursPerDay(dto.maxWorkingHoursPerDay());
        for (var day : scheduleDays) {
            ScheduleDay scheduleDay = new ScheduleDay();
            scheduleDay.setSchedule(schedule);
            scheduleDay.setStartDateTime(day.startDateTime());
            scheduleDay.setEndDateTime(day.endDateTime());
            scheduleDay.setShiftType(shiftTypeMap.get(day.shiftTypeUUID()));
            scheduleDay.setUuid(UUID.randomUUID());
            Set<CreateWorkBreakDTO> workBreaks = Optional.ofNullable(day.workBreaks()).orElse(Collections.emptySet());
            for (var workBreak : workBreaks) {
                if (Optional.ofNullable(workBreak.startDateTime()).isEmpty() || Optional.ofNullable(workBreak.endDateTime()).isEmpty()) {
                    throw new IllegalArgumentException("Argument startDateTime or endDateTime missing");
                }
                WorkBreak workBreak1 = new WorkBreak();
                workBreak1.setScheduleDay(scheduleDay);
                workBreak1.setUuid(UUID.randomUUID());
                workBreak1.setPaid(workBreak.isPaid());
                workBreak1.setStartDateTime(workBreak.startDateTime());
                workBreak1.setEndDateTime(workBreak.endDateTime());
                scheduleDay.getWorkBreaks().add(workBreak1);
            }
            schedule.getScheduleDays().add(scheduleDay);
        }
        userScheduleRepository.updateIsActiveUserIn(false, users);
        for (var user : users) {
            UserSchedule userSchedule1 = new UserSchedule();
            userSchedule1.getId().setUserId(user.getId());
            userSchedule1.getId().setScheduleId(schedule.getId());
            userSchedule1.setSchedule(schedule);
            userSchedule1.setUser(user);
            userSchedule1.setActive(true);
            System.out.println();
            userScheduleRepository.save(userSchedule1);
        }
        return schedule.getUuid();
    }

    private Map<UUID, ShiftType> fetchUUIDsToShiftTypeMap(Set<UUID> uuids) {
        uuids = Optional.ofNullable(uuids).orElseThrow(() -> new IllegalArgumentException("UUID set cannot be null"));
        int initialCapacity = (int) Math.ceil(uuids.size() / 0.75);
        Map<UUID, ShiftType> shiftTypeMap = new HashMap<>(initialCapacity);
        Set<ShiftType> shiftTypes = shiftTypeRepository.findAllByUuidIn(uuids);
        if (shiftTypes.size() != uuids.size()) {
            throw new ShiftTypeNotFound("Some of the shift types might not exist");
        }
        shiftTypes.forEach(shiftType -> shiftTypeMap.put(shiftType.getUuid(), shiftType));
        return shiftTypeMap;
    }

    @Override
    @Transactional
    public void updateSchedule(UpdateScheduleDTO dto) {
        Optional.ofNullable(dto).orElseThrow(() -> new IllegalArgumentException("Method argument cannot be null"));
        Optional.ofNullable(dto.schedule()).orElseThrow(
                () -> new IllegalArgumentException("Schedule cannot be null")
        );
        Optional.ofNullable(dto.scheduleUUID()).orElseThrow(
                () -> new IllegalArgumentException("UUID cannot be null")
        );
        Set<UpdateScheduleDayDTO> existingScheduleDays = Optional.ofNullable(
                dto.schedule().existingScheduleDays()
        ).orElse(Collections.emptySet());
        Set<CreateScheduleDayDTO> newScheduleDays = Optional.ofNullable(
                dto.schedule().newScheduleDays()
        ).orElse(Collections.emptySet());
        Set<UUID> deletedScheduleDays = Optional.ofNullable(
                dto.schedule().deletedScheduleDays()
        ).orElse(Collections.emptySet());
        existingScheduleDays.removeIf(day -> Optional.ofNullable(day).isEmpty());
        newScheduleDays.removeIf(day -> Optional.ofNullable(day).isEmpty());
        deletedScheduleDays.removeIf(uuid -> Optional.ofNullable(uuid).isEmpty());
        Set<UUID> shiftTypeUUIDs = new HashSet<>();
        existingScheduleDays.forEach(day ->
                Optional.ofNullable(day.scheduleDay()).ifPresent(d ->
                        Optional.ofNullable(d.shiftTypeUUID()).ifPresent(shiftTypeUUIDs::add)));
        newScheduleDays.forEach(day ->
                Optional.ofNullable(day.shiftTypeUUID()).ifPresent(shiftTypeUUIDs::add));
        Map<UUID, ShiftType> shiftTypeMap = fetchUUIDsToShiftTypeMap(shiftTypeUUIDs);
        Schedule schedule = scheduleRepository.findByUuid(dto.scheduleUUID())
                .orElseThrow(() -> new ScheduleNotFoundException(
                        String.format("Schedule of uuid %s not found", dto.scheduleUUID())
                ));
        Set<ScheduleDay> scheduleDays = schedule.getScheduleDays();
        for (UpdateScheduleDayDTO existingScheduleDay : existingScheduleDays) {
            if (Optional.ofNullable(existingScheduleDay.scheduleDayUUID()).isEmpty() ||
                    Optional.ofNullable(existingScheduleDay.scheduleDay()).isEmpty()) {
                continue;
            }
            ScheduleDay scheduleDay = scheduleDays.stream()
                    .filter(filteredScheduleDay ->
                            filteredScheduleDay.getUuid().equals(existingScheduleDay.scheduleDayUUID()))
                    .findFirst()
                    .orElseThrow(() -> new ScheduleNotFoundException(
                            String.format("Scheduled day of uuid %s not found", existingScheduleDay.scheduleDayUUID())
                    ));
            Set<WorkBreak> workBreaks = scheduleDay.getWorkBreaks();
            UpdateScheduleDayBodyDTO day = existingScheduleDay.scheduleDay();
            Optional.ofNullable(day.startDateTime()).ifPresent(scheduleDay::setStartDateTime);
            Optional.ofNullable(day.endDateTime()).ifPresent(scheduleDay::setEndDateTime);
            Optional.ofNullable(day.shiftTypeUUID()).ifPresent(uuid -> scheduleDay.setShiftType(shiftTypeMap.get(uuid)));
            Set<UpdateWorkBreakDTO> existingWorkBreaks = Optional.ofNullable(day.existingWorkBreaks()).orElse(Collections.emptySet());
            existingWorkBreaks.removeIf(Objects::isNull);
            for (var existingWorkBreak : existingWorkBreaks) {
                if (Optional.ofNullable(existingWorkBreak.workBreak()).isEmpty() ||
                        Optional.ofNullable(existingWorkBreak.workBreakUUID()).isEmpty()) {
                    continue;
                }
                WorkBreak workBreak = workBreaks.stream()
                        .filter(filteredWorkBreak ->
                                filteredWorkBreak.getUuid().equals(existingWorkBreak.workBreakUUID()))
                        .findFirst()
                        .orElseThrow(() -> new WorkBreakNotFound(
                                String.format("Work break of uuid %s not found", existingWorkBreak.workBreakUUID())
                        ));
                workBreak.setStartDateTime(existingWorkBreak.workBreak().startDateTime());
                workBreak.setEndDateTime(existingWorkBreak.workBreak().endDateTime());
                workBreak.setPaid(existingWorkBreak.workBreak().isPaid());
            }
            Set<CreateWorkBreakDTO> newWorkBreaks = Optional.ofNullable(day.newWorkBreaks()).orElse(Collections.emptySet());
            newWorkBreaks.removeIf(Objects::isNull);
            for (var newWorkBreak : newWorkBreaks) {
                WorkBreak workBreak = new WorkBreak();
                workBreak.setUuid(UUID.randomUUID());
                workBreak.setScheduleDay(scheduleDay);
                workBreak.setPaid(newWorkBreak.isPaid());
                workBreak.setStartDateTime(newWorkBreak.startDateTime());
                workBreak.setEndDateTime(newWorkBreak.endDateTime());
                scheduleDay.getWorkBreaks().add(workBreak);
            }
            Set<UUID> deletedWorkBreaksUUIDs = Optional.ofNullable(day.deletedWorkBreaks()).orElse(Collections.emptySet());
            deletedWorkBreaksUUIDs.removeIf(Objects::isNull);
            for (var deletedWorkBreakUUID : deletedWorkBreaksUUIDs) {
                Iterator<WorkBreak> iterator = scheduleDay.getWorkBreaks().iterator();
                while (iterator.hasNext()) {
                    WorkBreak workBreak = iterator.next();
                    if (workBreak.getUuid().equals(deletedWorkBreakUUID)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        for (CreateScheduleDayDTO newScheduleDay : newScheduleDays) {
            ScheduleDay scheduleDay = new ScheduleDay();
            scheduleDay.setStartDateTime(
                    Optional.ofNullable(newScheduleDay.startDateTime())
                            .orElseThrow(() -> new IllegalArgumentException("Empty startDateTime field in newScheduleDay element"))
            );
            scheduleDay.setEndDateTime(
                    Optional.ofNullable(newScheduleDay.endDateTime())
                            .orElseThrow(() -> new IllegalArgumentException("Empty endDateTime field in newScheduleDay element"))
            );
            scheduleDay.setShiftType(
                    shiftTypeMap.get(Optional.ofNullable(newScheduleDay.shiftTypeUUID())
                            .orElseThrow(() -> new IllegalArgumentException("Empty shiftTypeUUID field in newScheduleDay element")))
            );
            scheduleDay.setUuid(UUID.randomUUID());
            Set<CreateWorkBreakDTO> workBreaks = Optional.ofNullable(newScheduleDay.workBreaks()).orElse(Collections.emptySet());
            workBreaks.removeIf(Objects::isNull);
            for (var workBreak : workBreaks) {
                WorkBreak workBreak1 = new WorkBreak();
                workBreak1.setStartDateTime(
                        Optional.ofNullable(workBreak.startDateTime())
                                .orElseThrow(() -> new IllegalArgumentException("Empty startDateTime field in newWorkBreak element"))
                );
                workBreak1.setEndDateTime(
                        Optional.ofNullable(workBreak.endDateTime())
                                .orElseThrow(() -> new IllegalArgumentException("Empty endDateTime field in newWorkBreak element"))
                );
                workBreak1.setPaid(
                        Optional.of(workBreak.isPaid())
                                .orElseThrow(() -> new IllegalArgumentException("Empty isPaid field in newWorkBreak element"))
                );
                workBreak1.setUuid(UUID.randomUUID());
                workBreak1.setScheduleDay(scheduleDay);
                scheduleDay.getWorkBreaks().add(workBreak1);
            }
            schedule.getScheduleDays().add(scheduleDay);
            scheduleDay.setSchedule(schedule);
        }
        for (var deletedScheduleDayUUID : deletedScheduleDays) {
            Iterator<ScheduleDay> iterator = scheduleDays.iterator();
            while (iterator.hasNext()) {
                ScheduleDay scheduleDay = iterator.next();
                if (scheduleDay.getUuid().equals(deletedScheduleDayUUID)) {
                    iterator.remove();
                    break;
                }
            }
        }
        Optional.ofNullable(dto.schedule().maxWorkingHoursPerDay()).ifPresent(schedule::setMaxWorkingHoursPerDay);
        Optional.ofNullable(dto.schedule().description()).ifPresent(schedule::setDescription);
        Optional.ofNullable(dto.schedule().name()).ifPresent(schedule::setName);
        Optional.ofNullable(dto.schedule().enableHolidayAssignments()).ifPresent(schedule::setEnableHolidayAssignments);
        Optional.ofNullable(dto.schedule().enableWorkingSaturdays()).ifPresent(schedule::setEnableWorkingSaturdays);
        Optional.ofNullable(dto.schedule().enableWorkingSundays()).ifPresent(schedule::setEnableWorkingSundays);
        // todo: validate the schedule
        scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(UUID uuid) {
        Schedule schedule = scheduleRepository.findByUuid(uuid).orElseThrow(
                () -> new ScheduleNotFoundException(String.format("Schedule of uuid %s not found", uuid))
        );
        for (var scheduleDay : schedule.getScheduleDays()) {
            scheduleDay.getAttendances().stream()
                    .filter(attendance ->
                            Optional.ofNullable(attendance.getAttendanceStatus())
                                    .isEmpty())
                    .findAny()
                    .orElseThrow(() -> new CannotDeleteSchedule("The schedule cannot be deleted: past attendance records exist"));
        }
        scheduleRepository.delete(schedule);
    }

    @Override
    public ScheduleDTO getActiveSchedule(UUID uuid) {
        Optional.ofNullable(uuid).orElseThrow(() -> new IllegalArgumentException("UUID cannot be null"));
        User user = userRepository.findByUuid(uuid).orElseThrow(
                () -> new UserNotFoundException(String.format("User of uuid %s not found", uuid))
        );
        UserSchedule userSchedule = userScheduleRepository.findByIsActiveAndUser(true, user)
                .orElseThrow(() -> new ScheduleNotFoundException("Active schedule not found"));
        Schedule schedule = userSchedule.getSchedule();
        return ScheduleMapper.map(schedule);
    }

    @Override
    public ScheduleMetaListDTO getSchedules(FetchSchedulesFiltersDTO filters) {
        Optional.ofNullable(filters).orElseThrow(() -> new IllegalArgumentException("Filters argument cannot be null"));
        int pageNumber = Optional.ofNullable(filters.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(filters.pageSize()).orElse(10);
        Specification<Schedule> specification = Specification.where(null);
        Optional.ofNullable(filters.userUUID()).ifPresent(userUUID -> {
            User user = userRepository.findByUuid(userUUID).orElseThrow(() -> new UserNotFoundException(String.format("User of uuid %s not found", userUUID)));
            Specification<Schedule> hasUserUUID = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), user.getId());
            specification.and(hasUserUUID);
        });
        Optional.ofNullable(filters.isActive()).ifPresent(active -> {
            Specification<Schedule> isActive = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("active"), active);
            specification.and(isActive);
        });

        Page<Schedule> schedules = scheduleRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return new ScheduleMetaListDTO(
                schedules.getTotalElements(),
                schedules.getTotalPages(),
                schedules.getNumber(),
                schedules.getNumberOfElements(),
                schedules.getSize(),
                schedules.get().map(schedule -> new ScheduleMetaDTO(
                        schedule.getUuid(),
                        schedule.getName(),
                        schedule.getDescription(),
                        schedule.getMaxWorkingHoursPerDay(),
                        schedule.getEnableHolidayAssignments(),
                        schedule.getEnableWorkingSaturdays(),
                        schedule.getEnableWorkingSundays()
                )).toList()
        );
    }

    @Override
    public ScheduleDTO getSchedule(UUID uuid) {
        Optional.ofNullable(uuid).orElseThrow(() -> new IllegalArgumentException("UUID cannot be null"));
        Schedule schedule = scheduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ScheduleNotFoundException(String.format("Schedule of uuid %s not found", uuid)));
        return ScheduleMapper.map(schedule);
    }
}
