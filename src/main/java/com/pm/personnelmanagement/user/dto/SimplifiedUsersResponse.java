package com.pm.personnelmanagement.user.dto;

import java.util.List;

public record SimplifiedUsersResponse(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        List<SimplifiedUserDTO> users
) {
}
