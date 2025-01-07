package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "absence_excuse_statuses")
public class AbsenceExcuseStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "absenceExcuseStatus")
    private Set<DetailedAbsenceExcuseStatus> detailedAbsenceExcuseStatuses = new HashSet<>();

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

    public Set<DetailedAbsenceExcuseStatus> getDetailedAbsenceExcuseStatuses() {
        return detailedAbsenceExcuseStatuses;
    }

    public void setDetailedAbsenceExcuseStatuses(Set<DetailedAbsenceExcuseStatus> statuses) {
        this.detailedAbsenceExcuseStatuses = statuses;
    }
}
