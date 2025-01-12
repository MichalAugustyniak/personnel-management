package com.pm.personnelmanagement.schedule.utils;

import com.pm.personnelmanagement.schedule.exception.ScheduleDayNotFoundException;
import com.pm.personnelmanagement.schedule.model.ScheduleDay;
import com.pm.personnelmanagement.schedule.repository.ScheduleDayRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ScheduleDayUtils {
    private final ScheduleDayRepository scheduleDayRepository;

    public ScheduleDayUtils(ScheduleDayRepository scheduleDayRepository) {
        this.scheduleDayRepository = scheduleDayRepository;
    }

    public ScheduleDay fetchScheduleDay(@NotNull UUID uuid) {
        return scheduleDayRepository.findByUuid(uuid).orElseThrow(() ->
                new ScheduleDayNotFoundException(
                        String.format("Schedule day of uuid %s not found", uuid)
                ));
    }
}
