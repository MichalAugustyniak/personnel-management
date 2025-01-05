package com.pm.personnelmanagement.job.position.model;

import com.pm.personnelmanagement.user.model.User;
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
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
    @Column(name = "contract_uuid", unique = true, nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID contractUUID;
    @Column(name = "salary_uuid", unique = true, nullable = false)
    @JdbcTypeCode(Types.VARCHAR)
    private UUID salaryUUID;
    @Column(nullable = false)
    private Float workload;
}
