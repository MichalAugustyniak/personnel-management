package com.pm.personnelmanagement.document.common.template;

import com.pm.personnelmanagement.document.common.utils.DocumentLocationPath;
import com.pm.personnelmanagement.document.common.utils.Docx4JUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EmploymentCertificateDocxTemplate implements DocxTemplate {
    @Override
    public WordprocessingMLPackage getFilled(Map<String, String> templateValue) throws Docx4JException {
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
    }
}
