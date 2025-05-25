package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.CannotDeleteSchedule;
import com.pm.personnelmanagement.schedule.exception.ScheduleNotFoundException;
import com.pm.personnelmanagement.schedule.exception.ShiftTypeNotFoundException;
import com.pm.personnelmanagement.schedule.exception.WorkBreakNotFound;
import com.pm.personnelmanagement.schedule.mapper.ScheduleMapper;
import com.pm.personnelmanagement.schedule.model.*;
import com.pm.personnelmanagement.schedule.repository.ScheduleRepository;
import com.pm.personnelmanagement.schedule.repository.ShiftTypeRepository;
import com.pm.personnelmanagement.schedule.repository.UserScheduleRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Validated
@Service
public class DefaultScheduleService implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;
    private final ShiftTypeRepository shiftTypeRepository;

    public DefaultScheduleService(ScheduleRepository scheduleRepository, UserScheduleRepository userScheduleRepository, UserRepository userRepository, ShiftTypeRepository shiftTypeRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userScheduleRepository = userScheduleRepository;
        this.userRepository = userRepository;
        this.shiftTypeRepository = shiftTypeRepository;
    }

    @Override
    public UUID createSchedule(CreateScheduleDTO dto) {
        Set<User> users = userRepository.findAllByUsernameIn(dto.users());
        if (users.size() != dto.users().size()) {
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
        scheduleRepository.save(schedule);
        return schedule.getUuid();
    }

    private Map<UUID, ShiftType> fetchUUIDsToShiftTypeMap(Set<UUID> uuids) {
        uuids = Optional.ofNullable(uuids).orElseThrow(() -> new IllegalArgumentException("UUID set cannot be null"));
        int initialCapacity = (int) Math.ceil(uuids.size() / 0.75);
        Map<UUID, ShiftType> shiftTypeMap = new HashMap<>(initialCapacity);
        Set<ShiftType> shiftTypes = shiftTypeRepository.findAllByUuidIn(uuids);
        if (shiftTypes.size() != uuids.size()) {
            throw new ShiftTypeNotFoundException("Some of the shift types might not exist");
        }
        shiftTypes.forEach(shiftType -> shiftTypeMap.put(shiftType.getUuid(), shiftType));
        return shiftTypeMap;
    }

    @Override
    @Transactional
    public void updateSchedule(UpdateScheduleDTO dto) {
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
            scheduleDay.setStartDateTime(newScheduleDay.startDateTime());
            scheduleDay.setEndDateTime(newScheduleDay.endDateTime());
            scheduleDay.setShiftType(shiftTypeMap.get(newScheduleDay.shiftTypeUUID()));
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
        scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(ScheduleDeleteRequest request) {
        Schedule schedule = scheduleRepository.findByUuid(request.uuid()).orElseThrow(
                () -> new ScheduleNotFoundException(String.format("Schedule of uuid %s not found", request.uuid()))
        );
        for (var scheduleDay : schedule.getScheduleDays()) {
            scheduleDay.getAttendances().stream()
                    .filter(attendance ->
                            Optional.ofNullable(attendance.getAttendanceStatus())
                                    .isEmpty())
                    .findAny()
                    .ifPresent((attendance) -> {
                        throw new CannotDeleteSchedule("The schedule cannot be deleted: past attendance records exist");
                    });
        }
        scheduleRepository.delete(schedule);
    }

    @Override
    public SchedulesResponse getSchedules(AuthenticatedRequest<SchedulesRequest> request) {
        var requestBody = request.request();
        int pageNumber = Optional.ofNullable(requestBody.pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(requestBody.pageSize()).orElse(10);
        Specification<Schedule> specification = Specification.where(null);
        if (requestBody.user() != null) {
            User user = userRepository.findByUsername(requestBody.user())
                    .orElseThrow(() -> new UserNotFoundException(String.format("User of username %s not found", requestBody.user())));

            Specification<Schedule> hasUserUUID = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("userSchedules").get("user").get("id"), user.getId());

            specification = specification.and(hasUserUUID);
        }
        if (requestBody.isActive() != null) {
            Specification<Schedule> isActive = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("userSchedules").get("isActive"), requestBody.isActive());

            specification = specification.and(isActive);
        }
        Page<Schedule> schedules = scheduleRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return new SchedulesResponse(
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
    public ScheduleDTO getSchedule(ScheduleRequest request) {
        Schedule schedule = scheduleRepository.findByUuidWithSortedDaysAndBreaks(request.uuid())
                .orElseThrow(() -> new ScheduleNotFoundException(String.format("Schedule of uuid %s not found", request.uuid())));
        return ScheduleMapper.map(schedule);
    }

    @Transactional
    @Override
    public void attachUsersToSchedule(AttachUsersToScheduleDTO dto) {
        Set<User> users = userRepository.findAllByUsernameIn(dto.users());
        if (users.size() != dto.users().size()) {
            throw new UserNotFoundException("Some of the users not found");
        }
        Schedule schedule = scheduleRepository.findByUuid(dto.scheduleUUID())
                .orElseThrow(() -> new ScheduleNotFoundException(String.format("Schedule of uuid %s not found", dto.scheduleUUID())));
        userScheduleRepository.updateIsActiveUserIn(false, users);
        Set<UserSchedule> newUserSchedules = new HashSet<>();
        Set<UserSchedule> foundUserSchedules = userScheduleRepository.findAllByScheduleAndUserIn(schedule, users);
        for (var user : users) {
            for (var foundUserSchedule : foundUserSchedules) {
                if (user.equals(foundUserSchedule.getUser())) {
                    users.remove(user);
                    break;
                }
            }
        }
        for (var user : users) {
            UserSchedule userSchedule = new UserSchedule();
            userSchedule.setSchedule(schedule);
            userSchedule.setUser(user);
            userSchedule.setActive(true);
            newUserSchedules.add(userSchedule);
        }
        userScheduleRepository.updateIsActiveUserIn(true, foundUserSchedules.stream().map(UserSchedule::getUser).toList());
        userScheduleRepository.saveAll(newUserSchedules);
    }

    @Transactional
    @Override
    public void detachUsersFromSchedule(DetachUsersFromScheduleDTO dto) {
        Set<User> users = userRepository.findAllByUsernameIn(dto.users());
        if (users.size() != dto.users().size()) {
            throw new UserNotFoundException("Some of the users not found");
        }
        scheduleRepository.findByUuid(dto.scheduleUUID())
                .orElseThrow(() -> new ScheduleNotFoundException(String.format("Schedule of uuid %s not found", dto.scheduleUUID())));
        userScheduleRepository.updateIsActiveUserIn(false, users);
    }
}
