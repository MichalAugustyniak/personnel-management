package com.pm.personnelmanagement.document.employeecertificate.generator;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.time.LocalDate;

public interface EmployeeCertificateGenerator {
    WordprocessingMLPackage generateDocx(
            String city,
            String postalCode,
            LocalDate date,
            String firstName,
            String lastName,
            String pesel,
            String companyName,
            boolean isEmployed,
            int workloadHours,
            float workload,
            LocalDate firstDay,
            LocalDate lastDay,
            String workProfession
    );
}
