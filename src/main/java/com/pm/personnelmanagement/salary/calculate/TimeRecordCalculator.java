package com.pm.personnelmanagement.salary.calculate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeRecordCalculator {
    public static TimeRecord calculate(LocalDateTime start, LocalDateTime end) {
        LocalDateTime current = start;
        LocalDateTime nextNightStart = current.toLocalDate().atTime(22, 0, 0);
        LocalDateTime nextDayStart = current.toLocalDate().atTime(6, 0, 0);
        LocalTime dayStart = LocalTime.of(6, 0, 0);
        LocalTime nightStart = LocalTime.of(22, 0, 0);
        long daySeconds = 0;
        long nightSeconds = 0;
        while (current.isBefore(end)) {
            if ((current.toLocalTime().equals(dayStart) || current.toLocalTime().isAfter(dayStart)) && current.toLocalTime().isBefore(nightStart)) {
                // is day
                if (end.isBefore(nextNightStart)) {
                    daySeconds += Duration.between(current, end).getSeconds();
                    break;
                }
                long secondsToNight = Duration.between(current.toLocalTime(), nextNightStart).toSeconds();
                nextDayStart = nextDayStart.plusDays(1);
                current = current.plusSeconds(secondsToNight);
                daySeconds += secondsToNight;
            } else {
                // is night
                if (end.isBefore(nextDayStart)) {
                    nightSeconds += Duration.between(current, end).getSeconds();
                    break;
                }
                long secondsToDay = Duration.between(current, nextDayStart).toSeconds();
                nextNightStart = nextNightStart.plusDays(1);
                current = current.plusSeconds(secondsToDay);
                nightSeconds += secondsToDay;
            }
        }
        return new TimeRecord(daySeconds, nightSeconds);
    }
}
