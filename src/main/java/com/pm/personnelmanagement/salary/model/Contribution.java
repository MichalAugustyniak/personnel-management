package com.pm.personnelmanagement.salary.model;

import com.pm.personnelmanagement.job.model.ContractType;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "contributions")
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ContributionType contributionType;
    @Column(nullable = false)
    private ZonedDateTime effectiveFrom;
    @Column(nullable = false)
    private Float value;
    @ManyToOne
    private ContractType contractType;
}
