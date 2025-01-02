package com.pm.personnelmanagement.document.employeecertificate.generator;

import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import java.time.LocalDate;

public class EmployeeCertificateGeneratorV1 implements EmployeeCertificateGenerator {
    @Override
    public WordprocessingMLPackage generateDocx(
            String city,
            String postalCode,
            LocalDate date,
            String firstName,
            String lastName,
            String pesel,
            int workLoad,
            LocalDate firstDay,
            LocalDate lastDay,
            String workProfession
    ) {

        try {
            WordprocessingMLPackage wordMLPPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordMLPPackage.getMainDocumentPart();
            mainDocumentPart.addStyledParagraphOfText("Title", "Hello World!");
            mainDocumentPart.addParagraphOfText("Test paragraph");
            return wordMLPPackage;
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
