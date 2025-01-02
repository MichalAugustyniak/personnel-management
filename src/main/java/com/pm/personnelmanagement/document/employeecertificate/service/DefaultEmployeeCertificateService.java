package com.pm.personnelmanagement.document.employeecertificate.service;

import com.pm.personnelmanagement.document.common.dto.UserDocument;
import com.pm.personnelmanagement.document.employeecertificate.component.EmployeeCertificateGeneratorComponent;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultEmployeeCertificateService implements EmployeeCertificateService {
    private final EmployeeCertificateGeneratorComponent employeeCertificateGeneratorComponent;

    public DefaultEmployeeCertificateService(EmployeeCertificateGeneratorComponent employeeCertificateGeneratorComponent) {
        this.employeeCertificateGeneratorComponent = employeeCertificateGeneratorComponent;
    }

    @Override
    public UserDocument generateEmployeeCertificate(UUID userUUID) {
        return employeeCertificateGeneratorComponent.generate(userUUID);
    }
}
