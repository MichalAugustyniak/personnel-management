package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "attendance_statuses")
public class AttendanceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;    // Obecność, Nieobecność nieusprawiedliwiona, Nieobecność usprawiedliwiona, Urlop bezpłatny, Urlop płatny, Urlop chorobowy, L4 etc.
    private String description;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY, mappedBy = "attendanceStatus")
    private Set<Attendance> attendances = new HashSet<>();
    @JdbcTypeCode(Types.VARCHAR)
    @Column(nullable = false)
    private UUID uuid;
    @Column(nullable = false)
    private Boolean isExcusable;

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

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        this.attendances = attendances;
    }

    public Boolean getExcusable() {
        return isExcusable;
    }

    public void setExcusable(Boolean excusable) {
        isExcusable = excusable;
    }
}
