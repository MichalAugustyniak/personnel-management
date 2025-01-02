package com.pm.personnelmanagement.document.employeecertificate.controller;

import com.pm.personnelmanagement.document.common.dto.UserDocument;
import com.pm.personnelmanagement.document.employeecertificate.service.EmployeeCertificateService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/documents/employee-certificate")
public class DefaultEmployeeCertificateController implements EmployeeCertificateController {
    private final EmployeeCertificateService employeeCertificateService;

    public DefaultEmployeeCertificateController(EmployeeCertificateService employeeCertificateService) {
        this.employeeCertificateService = employeeCertificateService;
    }

    @GetMapping("/generate")
    @Override
    public ResponseEntity<Resource> generateEmployeeCertificate(@RequestParam UUID userUUID) {
        System.out.println("DefaultEmployeeCertificateController: Received GET request...");
        System.out.println("DefaultEmployeeCertificateController: Invoking employee certificate controller generate method...");
        UserDocument document = employeeCertificateService.generateEmployeeCertificate(userUUID);
        System.out.println("DefaultEmployeeCertificateController: Returning a response...");
        return ResponseEntity.ok()
                .contentType(document.mediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + document.filename())
                .body(document.resource());
    }
}
