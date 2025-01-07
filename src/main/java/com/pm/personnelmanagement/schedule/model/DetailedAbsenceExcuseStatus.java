package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "detailed_absence_excuse_statuses")
public class DetailedAbsenceExcuseStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AbsenceExcuseStatus absenceExcuseStatus;
    private String message;
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "detailedAbsenceExcuseStatus")
    private AbsenceExcuse absenceExcuse;
    private Boolean isChecked;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public AbsenceExcuse getAbsenceExcuse() {
        return absenceExcuse;
    }

    public void setAbsenceExcuse(AbsenceExcuse absenceExcuse) {
        this.absenceExcuse = absenceExcuse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbsenceExcuseStatus getAbsenceExcuseStatus() {
        return absenceExcuseStatus;
    }

    public void setAbsenceExcuseStatus(AbsenceExcuseStatus status) {
        this.absenceExcuseStatus = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
