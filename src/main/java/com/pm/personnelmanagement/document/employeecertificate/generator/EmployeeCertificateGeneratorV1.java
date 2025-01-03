package com.pm.personnelmanagement.document.employeecertificate.generator;

import com.pm.personnelmanagement.document.common.utils.DocumentLocationPath;
import com.pm.personnelmanagement.document.common.utils.Docx4JUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class EmployeeCertificateGeneratorV1 implements EmployeeCertificateGenerator {
    @Override
    public WordprocessingMLPackage generateDocx(
            String city,
            String postalCode,
            LocalDate date,
            String firstName,
            String lastName,
            String pesel,
            String companyName,
            boolean isEmployed,
            int workLoadHours,
            float workload,
            LocalDate firstDayDate,
            LocalDate lastDayDate,
            String workProfession
    ) {
        try {
            Map<String, String> templateValue = new HashMap<>();
            templateValue.put("{{postalCode}}", postalCode);
            templateValue.put("{{city}}", city);
            templateValue.put("{{date}}", date.toString());
            templateValue.put("{{firstName}}", firstName);
            templateValue.put("{{lastName}}", lastName);
            templateValue.put("{{pesel}}", pesel);
            templateValue.put("{{companyName}}", companyName);
            templateValue.put("{{workloadHours}}", String.valueOf(workLoadHours));
            templateValue.put("{{firstDayDate}}", firstDayDate.toString());
            templateValue.put("{{lastDayDate}}", lastDayDate.toString());
            templateValue.put("{{workPositionName}}", workProfession);
            templateValue.put("{{isEmployed}}", String.valueOf(isEmployed));
            templateValue.put("{{workload}}", String.valueOf(workload));
            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(
                    new File(DocumentLocationPath.CERTIFICATE_OF_EMPLOYMENT)
            );
            Docx4JUtils.replace(wordMLPackage, templateValue);
            Map<String, String> map = new HashMap<>();
            if (templateValue.containsKey("{{isEmployed}}") && Boolean.parseBoolean(templateValue.get("{{isEmployed}}"))) {
                map.put("jest/był*", "jest");
                Docx4JUtils.replace(wordMLPackage, map);
            } else if (templateValue.containsKey("{{isEmployed}}") && !Boolean.parseBoolean(templateValue.get("{{isEmployed}}"))) {
                map.put("jest/był*", "był(a)");
                Docx4JUtils.replace(wordMLPackage, map);
            }
            return wordMLPackage;
        } catch (Docx4JException e) {
            throw new RuntimeException(e);
        }
    }
}
