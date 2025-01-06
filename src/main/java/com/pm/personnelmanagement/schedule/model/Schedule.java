package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "schedule")
    private Set<ScheduleDay> scheduleDays = new HashSet<>();
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserSchedule> userSchedules = new HashSet<>();
    private Integer maxWorkingHoursPerDay;
    private Boolean enableHolidayAssignments;
    private Boolean enableWorkingSaturdays;
    private Boolean enableWorkingSundays;

    public Set<UserSchedule> getUserSchedules() {
        return userSchedules;
    }

    public void setUserSchedules(Set<UserSchedule> userSchedules) {
        this.userSchedules = userSchedules;
    }

    public Boolean getEnableHolidayAssignments() {
        return enableHolidayAssignments;
    }

    public void setEnableHolidayAssignments(Boolean enableHolidayAssignments) {
        this.enableHolidayAssignments = enableHolidayAssignments;
    }

    public Boolean getEnableWorkingSaturdays() {
        return enableWorkingSaturdays;
    }

    public void setEnableWorkingSaturdays(Boolean enableWorkingSaturdays) {
        this.enableWorkingSaturdays = enableWorkingSaturdays;
    }

    public Boolean getEnableWorkingSundays() {
        return enableWorkingSundays;
    }

    public void setEnableWorkingSundays(Boolean enableWorkingSundays) {
        this.enableWorkingSundays = enableWorkingSundays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ScheduleDay> getScheduleDays() {
        return scheduleDays;
    }

    public void setScheduleDays(Set<ScheduleDay> scheduleDays) {
        this.scheduleDays = scheduleDays;
    }

    public Integer getMaxWorkingHoursPerDay() {
        return maxWorkingHoursPerDay;
    }

    public void setMaxWorkingHoursPerDay(Integer maxWorkingHoursPerDay) {
        this.maxWorkingHoursPerDay = maxWorkingHoursPerDay;
    }
}
