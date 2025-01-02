package com.pm.personnelmanagement.document.employeecertificate.component;

import com.pm.personnelmanagement.document.common.dto.UserDocument;

import java.util.UUID;

public interface UserDocumentGenerator {
    UserDocument generate(UUID userUUID);
}
