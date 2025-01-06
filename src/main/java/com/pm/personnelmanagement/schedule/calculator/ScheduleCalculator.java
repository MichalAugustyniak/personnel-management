package com.pm.personnelmanagement.schedule.calculator;

import java.time.LocalDate;

public interface ScheduleCalculator {
    int calculateHours(LocalDate startDate, LocalDate endDate, float workload);
}
