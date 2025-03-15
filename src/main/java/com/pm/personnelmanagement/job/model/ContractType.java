package com.pm.personnelmanagement.job.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contract_types")
public class ContractType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
}
