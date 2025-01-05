package com.pm.personnelmanagement.job.position.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "job_positions")
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @ElementCollection
    private Set<String> responsibilities = new HashSet<>();
    @ElementCollection
    private Set<String> skills = new HashSet<>();
    @Column(unique = true, nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID uuid;
}
