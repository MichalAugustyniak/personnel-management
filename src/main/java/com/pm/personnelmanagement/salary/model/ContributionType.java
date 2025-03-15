package com.pm.personnelmanagement.salary.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contribution_types")
public class ContributionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Boolean isObligatory;
}
