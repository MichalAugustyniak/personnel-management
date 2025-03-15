package com.pm.personnelmanagement.config;

import java.util.List;

public record SessionDTO(
        String username,
        List<String> roles
) {
}
