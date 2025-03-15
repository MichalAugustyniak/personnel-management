package com.pm.personnelmanagement.salary.calculate;

import com.pm.personnelmanagement.job.model.Contract;
import com.pm.personnelmanagement.job.repository.AttendanceStatusWageMultiplierRepository;
import com.pm.personnelmanagement.job.repository.ContractRepository;
import com.pm.personnelmanagement.job.repository.UserJobPositionRepository;
import com.pm.personnelmanagement.job.util.ContractUtils;
import com.pm.personnelmanagement.salary.repository.SalaryRepository;
import com.pm.personnelmanagement.salary.util.SalaryUtils;
import com.pm.personnelmanagement.schedule.exception.ScheduleDayNotFoundException;
import com.pm.personnelmanagement.schedule.model.*;
import com.pm.personnelmanagement.schedule.repository.OvertimeHoursRepository;
import com.pm.personnelmanagement.schedule.repository.SubstitutionRepository;
import com.pm.personnelmanagement.schedule.repository.UserScheduleRepository;
import com.pm.personnelmanagement.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Component
public class DefaultMonthlySalaryCalculator implements MonthlySalaryCalculator {
    private final UserJobPositionRepository userJobPositionRepository;
    private final SalaryRepository salaryRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final ContractRepository contractRepository;
    private final ContractUtils contractUtils;
    private final OvertimeHoursRepository overtimeHoursRepository;
    private final SubstitutionRepository substitutionRepository;
    private final SalaryUtils salaryUtils;
    private final AttendanceStatusWageMultiplierRepository attendanceStatusWageMultiplierRepository;

    public DefaultMonthlySalaryCalculator(UserJobPositionRepository userJobPositionRepository, SalaryRepository salaryRepository, UserScheduleRepository userScheduleRepository, ContractRepository contractRepository, ContractUtils contractUtils, OvertimeHoursRepository overtimeHoursRepository, SubstitutionRepository substitutionRepository, SalaryUtils salaryUtils, AttendanceStatusWageMultiplierRepository attendanceStatusWageMultiplierRepository) {
        this.userJobPositionRepository = userJobPositionRepository;
        this.salaryRepository = salaryRepository;
        this.userScheduleRepository = userScheduleRepository;
        this.contractRepository = contractRepository;
        this.contractUtils = contractUtils;
        this.overtimeHoursRepository = overtimeHoursRepository;
        this.substitutionRepository = substitutionRepository;
        this.salaryUtils = salaryUtils;
        this.attendanceStatusWageMultiplierRepository = attendanceStatusWageMultiplierRepository;
    }

    public boolean isDayTimeBetween(LocalDateTime start, LocalDateTime end, LocalDateTime checked) {
        boolean isEqualStart = checked.isEqual(start);
        boolean isEqualEnd = checked.isEqual(end);
        boolean isAfterStart = checked.isAfter(start);
        boolean isBeforeEnd = checked.isBefore(end);
        return (isAfterStart && isBeforeEnd) || isEqualStart || isEqualEnd;
    }


    public int compareDayTimeSpace(DateTimeSpace dayTimeSpace1, DateTimeSpace dayTimeSpace2) {
        boolean leftSide = isDayTimeBetween(dayTimeSpace1.startDateTime(), dayTimeSpace1.endDateTime(), dayTimeSpace2.endDateTime());
        boolean rightSize = isDayTimeBetween(dayTimeSpace1.startDateTime(), dayTimeSpace1.endDateTime(), dayTimeSpace2.startDateTime());
        //System.out.printf("leftSize=%s\trightSize=%s%n", leftSide, rightSize);
        if (leftSide && rightSize) {
            return 2;
        } else if (leftSide) {
            return 0;
        } else if (rightSize) {
            return 1;
        } else {
            return -1;
        }
    }

    public List<DateTimeSpace> subtractDayTimeSpace(DateTimeSpace originalDayTimeSpace, DateTimeSpace subtractedDayTime) {
        int compareResult = compareDayTimeSpace(originalDayTimeSpace, subtractedDayTime);
        System.out.println("compareResult: " + compareResult);
        List<DateTimeSpace> dayTimeSpaces = new ArrayList<>();
        switch (compareResult) {
            case -1: {
                DateTimeSpace dayTimeSpace = new DateTimeSpace(originalDayTimeSpace.startDateTime(), originalDayTimeSpace.endDateTime());
                dayTimeSpaces.add(dayTimeSpace);
                break;
            }
            case 0: {
                DateTimeSpace dayTimeSpace = new DateTimeSpace(subtractedDayTime.endDateTime(), originalDayTimeSpace.endDateTime());
                dayTimeSpaces.add(dayTimeSpace);
                break;
            }
            case 1: {
                DateTimeSpace dayTimeSpace = new DateTimeSpace(originalDayTimeSpace.startDateTime(), subtractedDayTime.startDateTime());
                dayTimeSpaces.add(dayTimeSpace);
                break;
            }
            case 2: {
                DateTimeSpace dayTimeSpace = new DateTimeSpace(originalDayTimeSpace.startDateTime(), subtractedDayTime.startDateTime());
                DateTimeSpace dayTimeSpace1 = new DateTimeSpace(subtractedDayTime.endDateTime(), originalDayTimeSpace.endDateTime());
                if (!dayTimeSpace.startDateTime().isEqual(dayTimeSpace.endDateTime())) {
                    dayTimeSpaces.add(dayTimeSpace);
                }
                if (!dayTimeSpace1.startDateTime().isEqual(dayTimeSpace1.endDateTime())) {
                    dayTimeSpaces.add(dayTimeSpace1);
                }
                break;
            }
        }
        return dayTimeSpaces;
    }

    public List<DateTimeSpace> subtractDayTimeSpaceSetFromDayTimeSpace(DateTimeSpace dayTimeSpace, Set<DateTimeSpace> dayTimeSpaces) {
        Deque<DateTimeSpace> dayTimeSpaceDeque = new LinkedList<>();
        dayTimeSpaceDeque.add(new DateTimeSpace(dayTimeSpace.startDateTime(), dayTimeSpace.endDateTime()));
        for (var subtractedDayTimeSpace : dayTimeSpaces) {
            System.out.println("Checked: " + subtractedDayTimeSpace);
            DateTimeSpace polledDateTimeSpace = dayTimeSpaceDeque.pollFirst();
            List<DateTimeSpace> subtractResult = new ArrayList<>();
            while (polledDateTimeSpace != null) {
                System.out.println("Polled dateTimeSpace: " + polledDateTimeSpace);
                DateTimeSpace subtracted = new DateTimeSpace(subtractedDayTimeSpace.startDateTime(), subtractedDayTimeSpace.endDateTime());
                System.out.println("subtracting: " + subtracted);
                List<DateTimeSpace> subtractPart = subtractDayTimeSpace(polledDateTimeSpace, subtracted);
                subtractResult.addAll(subtractPart);
                System.out.println("subtractPart: " + subtractPart);
                polledDateTimeSpace = dayTimeSpaceDeque.pollFirst();
            }
            System.out.println("Adding subtract result: " + subtractResult);
            dayTimeSpaceDeque.addAll(subtractResult);
        }
        return new ArrayList<>(dayTimeSpaceDeque);
    }

    private List<DateTimeSpace> subtractNonPaidWorkBreaksFromAttendanceDayTimeSpace(Attendance attendance, Set<WorkBreak> workBreaks) {
        DateTimeSpace dayDateTimeSpace = new DateTimeSpace(attendance.getStartDateTime(), attendance.getEndDateTime());
        Set<DateTimeSpace> workBreakDateTimeSpaces = new HashSet<>();
        for (var workBreak : workBreaks) {
            if (!workBreak.getPaid()) {
                continue;
            }
            workBreakDateTimeSpaces.add(new DateTimeSpace(workBreak.getStartDateTime(), workBreak.getEndDateTime()));
        }
        return subtractDayTimeSpaceSetFromDayTimeSpace(dayDateTimeSpace, workBreakDateTimeSpaces);
    }

    /*
    private List<DayTimeSpace> subtractNonPaidWorkBreaksFromAttendanceDayTimeSpace(Attendance attendance, Set<WorkBreak> workBreaks) {
        Deque<DayTimeSpace> dayTimeSpaceDeque = new LinkedList<>();
        dayTimeSpaceDeque.add(new DayTimeSpace(attendance.getStartDateTime(), attendance.getEndDateTime()));
        for (var workBreak : workBreaks) {
            DayTimeSpace dayTimeSpace = dayTimeSpaceDeque.pollFirst();
            List<DayTimeSpace> subtractResult = new ArrayList<>();
            while (dayTimeSpace != null) {
                DayTimeSpace subtracted = new DayTimeSpace(workBreak.getStartDateTime(), workBreak.getEndDateTime());
                subtractResult.addAll(subtractDayTimeSpace(dayTimeSpace, subtracted));
                dayTimeSpace = dayTimeSpaceDeque.pollFirst();
            }
            dayTimeSpaceDeque.addAll(subtractResult);
        }
        return new ArrayList<>(dayTimeSpaceDeque);
    }
     */

    @Override
    public BigDecimal calculate(@NotNull User user, @NotNull YearMonth yearMonth) {
        UserSchedule userSchedule = userScheduleRepository.findByIsActiveAndUser(true, user)
                .orElseThrow(() -> new ScheduleDayNotFoundException("Active schedule for user not found"));
        Schedule schedule = userSchedule.getSchedule();
        Contract contract = contractUtils.fetchContract(user);

        // Calculate seconds for regular work time and breaks

        //List<ScheduleDayTypeAttendanceTime> scheduleDayTypeAttendanceTimes = new ArrayList<>();
        Map<Class<? extends ScheduleDay>, Map<AttendanceStatus, TimeRecord>> scheduleDayTypeAttendanceTimes = new HashMap<>();
        TimeRecord paidWorkBreakInSeconds = new TimeRecord(0, 0);
        TimeRecord workInSeconds = new TimeRecord(0, 0);

        // Iterate for all schedule days for provided user
        Set<ScheduleDay> scheduleDays = schedule.getScheduleDays();
        for (var scheduleDay : scheduleDays) {
            Map<AttendanceStatus, TimeRecord> attendanceStatusDurationMap;
            if (scheduleDayTypeAttendanceTimes.containsKey(scheduleDay.getClass())) {
                attendanceStatusDurationMap = scheduleDayTypeAttendanceTimes.get(scheduleDay.getClass());
            } else {
                attendanceStatusDurationMap = new HashMap<>();
            }
            // Check if schedule day not in specified month
            if (!scheduleDay.getStartDateTime().getMonth().equals(yearMonth.getMonth()) ||
                    scheduleDay.getStartDateTime().getYear() != yearMonth.getYear()) {
                continue;
            }
            // iterate for all attendances
            for (var attendance : scheduleDay.getAttendances()) {
                List<DateTimeSpace> dateTimeSpaces = subtractNonPaidWorkBreaksFromAttendanceDayTimeSpace(attendance, scheduleDay.getWorkBreaks());
                var status = attendance.getAttendanceStatus();
                long daySeconds = 0;
                long nightSeconds = 0;
                for (var dateTimeSpace : dateTimeSpaces) {
                    TimeRecord calculatedTime = TimeRecordCalculator.calculate(dateTimeSpace.startDateTime(), dateTimeSpace.endDateTime());
                    daySeconds += calculatedTime.daySeconds();
                    nightSeconds += calculatedTime.nightSecond();
                }
                TimeRecord calculatedTime = new TimeRecord(daySeconds, nightSeconds);
                if (!attendanceStatusDurationMap.containsKey(status)) {
                    attendanceStatusDurationMap.put(status, calculatedTime);
                } else {
                    TimeRecord oldValue = attendanceStatusDurationMap.get(attendance.getAttendanceStatus());
                    attendanceStatusDurationMap.put(status,
                            new TimeRecord(oldValue.daySeconds() + calculatedTime.daySeconds(), oldValue.nightSecond() + calculatedTime.nightSecond()));
                }

            }
            scheduleDayTypeAttendanceTimes.put(scheduleDay.getClass(), attendanceStatusDurationMap);
        }

        /*
        Salary salary = salaryUtils.fetchSalary(contract.getSalaryUUID());
        if (!salary.getClass().equals(MonthlySalary.class)) {
            throw new RuntimeException(String.format("Cannot cast Salary (%s) to MonthlySalary",
                    salary.getClass()));
        }
        MonthlySalary monthlySalary = (MonthlySalary) salary;
        //BigDecimal ratePerHour = monthlySalary.getMonthlyAmount().divide(new BigDecimal(workInSeconds / 3600), RoundingMode.CEILING);
        // płąca godzinowa obliczana jest z podzielenia stawki podstawowej czy premii z nocki???
        BigDecimal calculatedSalary = new BigDecimal(0);
        Set<AttendanceStatusWageMultiplier> attendanceStatusWageMultiplierSet =
                attendanceStatusWageMultiplierRepository.findAllByAttendanceStatusIn(attendanceStatusDurationMap.keySet());
        if (attendanceStatusWageMultiplierSet.size() != attendanceStatusDurationMap.size()) {
            //throw new RuntimeException("śpiący jestem");
            throw new AttendanceStatusWageMultiplierException("Cannot find all attendance status wage multiplier");
        }

        for (var setElement : attendanceStatusWageMultiplierSet) {
            //var value = attendanceStatusDurationMap.get(setElement.getAttendanceStatus()) * setElement.getMultiplier();
            //calculatedSalary = calculatedSalary.add(new BigDecimal(value));
        }

         */

        return null;
    }
}
