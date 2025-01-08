package com.pm.personnelmanagement.schedule.service;

import com.pm.personnelmanagement.schedule.dto.CreateScheduleDTO;
import com.pm.personnelmanagement.schedule.dto.CreateScheduleDayDTO;
import com.pm.personnelmanagement.schedule.dto.CreateWorkBreakDTO;
import com.pm.personnelmanagement.schedule.repository.ScheduleDayRepository;
import com.pm.personnelmanagement.schedule.repository.ScheduleRepository;
import com.pm.personnelmanagement.schedule.repository.UserScheduleRepository;
import com.pm.personnelmanagement.schedule.repository.WorkBreakRepository;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
class DefaultScheduleServiceTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserScheduleRepository userScheduleRepository;

    @Autowired
    private WorkBreakRepository workBreakRepository;

    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @Autowired
    private DefaultScheduleService scheduleService;

    @Test
    public void testCreateSchedule() {
        long userCount = userRepository.count();
        long workBreakCount = workBreakRepository.count();
        long scheduleDayCount = scheduleDayRepository.count();
        long scheduleCount = scheduleRepository.count();
        long userScheduleCount = userScheduleRepository.count();
        UUID uuid = UUID.randomUUID();
        User user = new User();
        user.setUuid(uuid);
        user.setCreatedAt(LocalDateTime.now());
        user.setAvatarUUID(UUID.randomUUID());
        user.setActive(true);
        user.setLastLoginAt(LocalDateTime.now());
        Random random = new Random();
        user.setIdentity_id("test_identity" + random.nextInt(1000));
        userRepository.save(user);
        Set<UUID> uuids = new HashSet<>();
        uuids.add(uuid);
        Set<CreateScheduleDayDTO> days = new HashSet<>();
        Set<CreateWorkBreakDTO> workBreaks = new HashSet<>();
        workBreaks.add(new CreateWorkBreakDTO(
                LocalDateTime.of(2025, Month.JANUARY, 10, 10, 0, 0),
                LocalDateTime.of(2025, Month.JANUARY, 10, 10, 15, 0),
                false
        ));
        workBreaks.add(new CreateWorkBreakDTO(
                LocalDateTime.of(2025, Month.JANUARY, 10, 15, 0, 0),
                LocalDateTime.of(2025, Month.JANUARY, 10, 15, 30, 0),
                true
        ));

        days.add(new CreateScheduleDayDTO(
                LocalDateTime.of(2025, Month.JANUARY, 10, 8, 0, 0),
                LocalDateTime.of(2025, Month.JANUARY, 10, 16, 0, 0),
                workBreaks
        ));
        Set<CreateWorkBreakDTO> workBreaks2 = new HashSet<>();
        workBreaks2.add(new CreateWorkBreakDTO(
                LocalDateTime.of(2025, Month.JANUARY, 11, 10, 0, 0),
                LocalDateTime.of(2025, Month.JANUARY, 11, 10, 15, 0),
                false
        ));
        workBreaks2.add(new CreateWorkBreakDTO(
                LocalDateTime.of(2025, Month.JANUARY, 11, 15, 0, 0),
                LocalDateTime.of(2025, Month.JANUARY, 11, 15, 30, 0),
                true
        ));

        days.add(new CreateScheduleDayDTO(
                LocalDateTime.of(2025, Month.JANUARY, 11, 8, 0, 0),
                LocalDateTime.of(2025, Month.JANUARY, 11, 16, 0, 0),
                workBreaks
        ));
        CreateScheduleDTO schedule = new CreateScheduleDTO(
                "Test name",
                "Test description",
                8,
                false,
                false,
                false,
                uuids,
                days
        );
        scheduleService.createSchedule(schedule);
        assertEquals(userCount + 1, userRepository.count());
        assertEquals(workBreakCount + 4, workBreakRepository.count());
        assertEquals(scheduleDayCount + 2, scheduleDayRepository.count());
        assertEquals(scheduleCount + 1, scheduleRepository.count());
        assertEquals(userScheduleCount + 1, userScheduleRepository.count());
    }
}