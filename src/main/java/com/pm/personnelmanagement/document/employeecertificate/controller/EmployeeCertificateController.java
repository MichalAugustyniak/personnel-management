package com.pm.personnelmanagement.document.employeecertificate.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface EmployeeCertificateController {
    public ResponseEntity<Resource> generateEmployeeCertificate(UUID userUUID);
}
