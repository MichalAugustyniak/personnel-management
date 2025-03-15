package com.pm.personnelmanagement.user.dto;

import com.pm.personnelmanagement.user.model.Sex;

import java.util.UUID;

public record UsersRequest(
    String role,
    UUID addressUUID,
    Sex sex,
    Boolean isActive,
    String like,
    int pageNumber,
    int pageSize
) {
}
