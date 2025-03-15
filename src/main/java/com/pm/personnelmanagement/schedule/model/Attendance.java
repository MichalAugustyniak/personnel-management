package com.pm.personnelmanagement.schedule.model;

import com.pm.personnelmanagement.user.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "attendances")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private AttendanceStatus attendanceStatus;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private ScheduleDay scheduleDay;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    //@ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    //private AbsenceExcuse absenceExcuse;
    @ManyToMany
    @JoinTable(
            name = "attendance_absence_excuse", // nazwa tabeli łączącej
            joinColumns = @JoinColumn(name = "attendance_id"), // klucz obcy do encji User
            inverseJoinColumns = @JoinColumn(name = "absence_excuse_id") // klucz obcy do encji Project
    )
    private Set<AbsenceExcuse> absenceExcuses = new HashSet<>();

    public Set<AbsenceExcuse> getAbsenceExcuses() {
        return absenceExcuses;
    }

    public void setAbsenceExcuses(Set<AbsenceExcuse> absenceExcuses) {
        this.absenceExcuses = absenceExcuses;
    }
/*
    public AbsenceExcuse getAbsenceExcuse() {
        return absenceExcuse;
    }

    public void setAbsenceExcuse(AbsenceExcuse absenceExcuse) {
        this.absenceExcuse = absenceExcuse;
    }

     */

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(AttendanceStatus status) {
        this.attendanceStatus = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ScheduleDay getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(ScheduleDay scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
