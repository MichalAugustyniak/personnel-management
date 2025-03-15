package com.pm.personnelmanagement.user.dto;

import java.util.List;

public record UsersResponse(
        long totalElements,
        int totalPages,
        int number,
        int numberOfElements,
        int size,
        List<UserDTO> users
) {
}
