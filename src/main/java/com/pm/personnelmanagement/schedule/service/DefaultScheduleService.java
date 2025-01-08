package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.file.exception.NotImplementedException;
import com.pm.personnelmanagement.schedule.dto.*;
import com.pm.personnelmanagement.schedule.exception.ScheduleNotFoundException;
import com.pm.personnelmanagement.schedule.exception.WorkBreakNotFound;
import com.pm.personnelmanagement.schedule.mapper.ScheduleMapper;
import com.pm.personnelmanagement.schedule.model.Schedule;
import com.pm.personnelmanagement.schedule.model.ScheduleDay;
import com.pm.personnelmanagement.schedule.model.UserSchedule;
import com.pm.personnelmanagement.schedule.model.WorkBreak;
import com.pm.personnelmanagement.schedule.repository.ScheduleRepository;
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

    public DefaultScheduleService(ScheduleRepository scheduleRepository, UserScheduleRepository userScheduleRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userScheduleRepository = userScheduleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UUID createSchedule(CreateScheduleDTO dto) {
        Set<User> users = userRepository.findAllByUuidIn(dto.userUUIDList());
        if (users.size() != dto.userUUIDList().size()) {
            throw new UserNotFoundException("Some of the users might not exist");
        }
        // The code below checks if all users have been found
        /*
        int initialCapacity = (int) Math.ceil(users.size() / 0.75);
        Map<UUID, User> userMap = new HashMap<>(initialCapacity);
        users.forEach(user -> userMap.put(
                user.getUuid(),
                user
        ));
        dto.userUUIDList().forEach(userUUID ->
                Optional.ofNullable(userMap.get(userUUID))
                        .orElseThrow(
                                () -> new UserNotFoundException(String.format("User of uuid %s not found", userUUID.toString()))
                        )
        );

         */
        Schedule schedule = new Schedule();
        schedule.setUuid(UUID.randomUUID());
        schedule.setName(dto.name());
        schedule.setDescription(dto.description());
        schedule.setEnableHolidayAssignments(dto.enableHolidayAssignments());
        schedule.setEnableWorkingSaturdays(dto.enableWorkingSaturdays());
        schedule.setEnableWorkingSundays(dto.enableWorkingSundays());
        schedule.setMaxWorkingHoursPerDay(dto.maxWorkingHoursPerDay());
        dto.scheduleDays().forEach(day -> {
            ScheduleDay scheduleDay = new ScheduleDay();
            scheduleDay.setSchedule(schedule);
            scheduleDay.setStartDateTime(day.startDateTime());
            scheduleDay.setEndDateTime(day.endDateTime());
            scheduleDay.setUuid(UUID.randomUUID());
            day.workBreaks().forEach(workBreak -> {
                WorkBreak workBreak1 = new WorkBreak();
                workBreak1.setScheduleDay(scheduleDay);
                workBreak1.setUuid(UUID.randomUUID());
                workBreak1.setPaid(workBreak.isPaid());
                workBreak1.setStartDateTime(workBreak.startDateTime());
                workBreak1.setEndDateTime(workBreak.endDateTime());
                scheduleDay.getWorkBreaks().add(workBreak1);
            });
            schedule.getScheduleDays().add(scheduleDay);
        });
        userScheduleRepository.updateIsActiveUserIn(false, users);
        users.forEach(user -> {
            UserSchedule userSchedule1 = new UserSchedule();
            userSchedule1.getId().setUserId(user.getId());
            userSchedule1.getId().setScheduleId(schedule.getId());
            userSchedule1.setSchedule(schedule);
            userSchedule1.setUser(user);
            userSchedule1.setActive(true);
            System.out.println();
            userScheduleRepository.save(userSchedule1);
        });
        return schedule.getUuid();
    }

    @Override
    @Transactional
    public void updateSchedule(UpdateScheduleDTO dto) {
        Schedule schedule = scheduleRepository.findByUuid(dto.scheduleUUID())
                .orElseThrow(() -> new ScheduleNotFoundException(
                        String.format("Schedule of uuid %s not found", dto.scheduleUUID())
                ));
        Set<ScheduleDay> scheduleDays = schedule.getScheduleDays();
        dto.schedule().existingScheduleDays().forEach(existingScheduleDay -> {
            ScheduleDay scheduleDay = scheduleDays.stream()
                    .filter(filteredScheduleDay ->
                            filteredScheduleDay.getUuid().equals(existingScheduleDay.scheduleDayUUID()))
                    .findFirst()
                    .orElseThrow(() -> new ScheduleNotFoundException(
                            String.format("Scheduled day of uuid %s not found", existingScheduleDay.scheduleDayUUID().toString())
                    ));
            existingScheduleDay.scheduleDay().startDateTime().ifPresent(scheduleDay::setStartDateTime);
            existingScheduleDay.scheduleDay().endDateTime().ifPresent(scheduleDay::setEndDateTime);
            Set<WorkBreak> workBreaks = scheduleDay.getWorkBreaks();
            existingScheduleDay.scheduleDay().workBreaks().forEach(workBreak -> {
                WorkBreak workBreak1 = workBreaks.stream()
                        .filter(filteredWorkBreak ->
                                filteredWorkBreak.getUuid().equals(workBreak.workBreakUUID()))
                        .findFirst()
                        .orElseThrow(() -> new WorkBreakNotFound(
                                String.format("Work break of uuid %s not found", workBreak.workBreakUUID())
                        ));
                workBreak.workBreak().startDateTime().ifPresent(workBreak1::setStartDateTime);
                workBreak.workBreak().endDateTime().ifPresent(workBreak1::setEndDateTime);
                workBreak.workBreak().isPaid().ifPresent(workBreak1::setPaid);
            });
        });
        dto.schedule().maxWorkingHoursPerDay().ifPresent(schedule::setMaxWorkingHoursPerDay);
        dto.schedule().description().ifPresent(schedule::setDescription);
        dto.schedule().name().ifPresent(schedule::setName);
        dto.schedule().enableHolidayAssignments().ifPresent(schedule::setEnableHolidayAssignments);
        dto.schedule().enableWorkingSaturdays().ifPresent(schedule::setEnableWorkingSaturdays);
        dto.schedule().enableWorkingSundays().ifPresent(schedule::setEnableWorkingSundays);
        // todo: validate the schedule
        scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(UUID uuid) {
        throw new NotImplementedException("Delete not implemented");
    }

    @Override
    public ScheduleDTO getActiveSchedule(UUID uuid) {
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
        int pageNumber = filters.pageNumber().orElse(0);
        int pageSize = filters.pageSize().orElse(10);
        Specification<Schedule> specification = Specification.where(null);
        filters.userUUID().ifPresent(userUUID -> {
            User user = userRepository.findByUuid(userUUID).orElseThrow(() -> new UserNotFoundException(String.format("User of uuid %s not found", userUUID)));
            Specification<Schedule> hasUserUUID = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), user.getId());
            specification.and(hasUserUUID);
        });
        filters.isActive().ifPresent(active -> {
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
}
