package com.pm.personnelmanagement.schedule.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "attendace_excuse_statuses")
public class AttendanceExcuseStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Attendance> attendances = new HashSet<>();
}
