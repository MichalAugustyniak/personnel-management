package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "schedule_days")
public class ScheduleDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Schedule schedule;

    public Set<WorkBreak> getWorkBreaks() {
        return workBreaks;
    }

    public void setWorkBreaks(Set<WorkBreak> workBreaks) {
        this.workBreaks = workBreaks;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "scheduleDay")
    private Set<WorkBreak> workBreaks = new HashSet<>();
}
