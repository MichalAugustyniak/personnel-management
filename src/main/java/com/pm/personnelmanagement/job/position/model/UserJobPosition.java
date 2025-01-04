package com.pm.personnelmanagement.job.position.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Table(name = "users_job_positions")
public class UserJobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private JobPosition jobPosition;
    @Column(name = "user_uuid", unique = true, nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID userUUID;
}
