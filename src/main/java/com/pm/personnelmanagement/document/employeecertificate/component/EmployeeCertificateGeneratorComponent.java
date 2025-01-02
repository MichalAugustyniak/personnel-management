package com.pm.personnelmanagement.document.employeecertificate.component;


import com.pm.personnelmanagement.document.common.dto.UserDocument;
import com.pm.personnelmanagement.document.employeecertificate.generator.EmployeeCertificateGenerator;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

@Component
public class EmployeeCertificateGeneratorComponent implements UserDocumentGenerator {
    private final EmployeeCertificateGenerator employeeCertificateGenerator;
    private final String city;
    private final String postalCode;

    public EmployeeCertificateGeneratorComponent(
            EmployeeCertificateGenerator employeeCertificateGenerator,
            @Value("app.address.city-name") String city,
            @Value("app.address.postal-code") String postalCode
    ) {
        this.employeeCertificateGenerator = employeeCertificateGenerator;
        this.city = city;
        this.postalCode = postalCode;
    }
    // maybe only employee uuid??
    public WordprocessingMLPackage generateDocx(UUID userUUID) {
        // todo: read user from keycloak api (/userinfo)
        // temporary data:
        String firstName = "Test_firsName";
        String lastName = "Test_lastName";
        String pesel = "0123456789";
        int workLoadHours = 40;
        LocalDate firstDay = LocalDate.of(2024, Month.AUGUST, 1);
        LocalDate lastDay = LocalDate.of(2024, Month.NOVEMBER, 1);
        String workProfession = "Test_workProfession";
        return employeeCertificateGenerator.generateDocx(
                city,
                postalCode,
                LocalDate.now(),
                firstName,
                lastName,
                pesel,
                workLoadHours,
                firstDay,
                lastDay,
                workProfession
        );
    }

    @Override
    public UserDocument generate(UUID userUUID) {
        try {
            WordprocessingMLPackage wordprocessingMLPackage = generateDocx(userUUID);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            wordprocessingMLPackage.save(byteArrayOutputStream);
            return new UserDocument(
                    new ByteArrayResource(byteArrayOutputStream.toByteArray()),
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
                    "document.docx"
            );
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
    }
}
