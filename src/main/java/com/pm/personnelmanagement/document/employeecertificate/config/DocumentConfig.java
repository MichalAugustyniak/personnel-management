package com.pm.personnelmanagement.document.employeecertificate.config;

import com.pm.personnelmanagement.document.employeecertificate.generator.EmployeeCertificateGenerator;
import com.pm.personnelmanagement.document.employeecertificate.generator.EmployeeCertificateGeneratorV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentConfig {

    @Bean
    public EmployeeCertificateGenerator employeeCertificateGeneratorV1() {
        return new EmployeeCertificateGeneratorV1();
    }
}
