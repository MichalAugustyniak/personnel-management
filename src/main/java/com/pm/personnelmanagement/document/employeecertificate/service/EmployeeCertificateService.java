package com.pm.personnelmanagement.document.employeecertificate.service;

import com.pm.personnelmanagement.document.common.dto.UserDocument;

import java.util.UUID;

public interface EmployeeCertificateService {
    UserDocument generateEmployeeCertificate(UUID userUUID);
}
