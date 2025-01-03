package com.pm.personnelmanagement.document.common.template;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.util.Map;

public interface DocxTemplate {
    WordprocessingMLPackage getFilled(Map<String, String> templateValue) throws Docx4JException;
}
