package com.pm.personnelmanagement.task.dto;

import com.pm.personnelmanagement.user.dto.SimplifiedUserDTO;

import java.util.List;

public record TaskUsers(
        List<SimplifiedUserDTO> users
) {
}
