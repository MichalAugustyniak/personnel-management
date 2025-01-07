package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.*;
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
import org.springframework.stereotype.Service;

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
    public void updateSchedule(UpdateScheduleDTO dto) {

    }

    @Override
    public void deleteSchedule(UUID uuid) {

    }

    @Override
    public ScheduleDTO getActiveSchedule(UUID uuid) {
        return null;
    }

    @Override
    public Page<ScheduleUUIDDTO> getSchedules(FetchSchedulesFiltersDTO filters) {
        return null;
    }
}
