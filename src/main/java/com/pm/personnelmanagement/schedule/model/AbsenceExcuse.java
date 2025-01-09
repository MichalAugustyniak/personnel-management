package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "absence_excuses")
public class AbsenceExcuse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "absenceExcuse")
    private Set<Attendance> attendances = new HashSet<>();
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID fileUUID;
    private String name;
    private String description;
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private DetailedAbsenceExcuseStatus detailedAbsenceExcuseStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        this.attendances = attendances;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getFileUUID() {
        return fileUUID;
    }

    public void setFileUUID(UUID fileUUID) {
        this.fileUUID = fileUUID;
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

    public DetailedAbsenceExcuseStatus getDetailedAbsenceExcuseStatus() {
        return detailedAbsenceExcuseStatus;
    }

    public void setDetailedAbsenceExcuseStatus(DetailedAbsenceExcuseStatus detailedStatus) {
        this.detailedAbsenceExcuseStatus = detailedStatus;
    }
}
