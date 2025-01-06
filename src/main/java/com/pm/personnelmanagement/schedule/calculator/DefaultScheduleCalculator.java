package com.pm.personnelmanagement.schedule.calculator;

import java.time.LocalDate;
import java.util.List;

public class DefaultScheduleCalculator implements ScheduleCalculator {
    //private final List<LocalDate> workFreeDays;
    @Override
    public int calculateHours(LocalDate startDate, LocalDate endDate, float workload) {
        if (workload <= 0f || workload > 1f) {
            throw new IllegalArgumentException(
                    String.format("Argument workload (%f) not in range (0;1>", workload)
            );
        }
        return 0;
    }
}
