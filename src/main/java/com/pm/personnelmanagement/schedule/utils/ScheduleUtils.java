package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.ScheduleNotFoundException;
import com.pm.personnelmanagement.schedule.exception.UserScheduleNotFoundException;
import com.pm.personnelmanagement.schedule.model.Schedule;
import com.pm.personnelmanagement.schedule.model.UserSchedule;
import com.pm.personnelmanagement.schedule.repository.ScheduleRepository;
import com.pm.personnelmanagement.schedule.repository.UserScheduleRepository;
import com.pm.personnelmanagement.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ScheduleUtils {
    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;

    public ScheduleUtils(ScheduleRepository scheduleRepository, UserScheduleRepository userScheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userScheduleRepository = userScheduleRepository;
    }

    public Schedule fetchSchedule(@NotNull UUID uuid) {
        return scheduleRepository.findByUuid(uuid).orElseThrow(
                () -> new ScheduleNotFoundException(
                        String.format("Schedule of uuid %s not found", uuid)
                )
        );
    }

    public Schedule fetchScheduleById(long id) {
        return scheduleRepository.findById(id).orElseThrow(
                () -> new ScheduleNotFoundException(
                        String.format("Schedule of id %s not found", id)
                )
        );
    }

    public UserSchedule fetchUserSchedule(@NotNull User user, Schedule schedule) {
        return userScheduleRepository.findByUserAndSchedule(user, schedule)
                .orElseThrow(() -> new UserScheduleNotFoundException(
                        "Association between user and schedule not found"
                ));
    }
}
