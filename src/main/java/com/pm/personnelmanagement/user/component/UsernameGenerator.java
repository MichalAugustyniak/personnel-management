package com.pm.personnelmanagement.user.component;

import jakarta.validation.constraints.NotEmpty;

public interface UsernameGenerator {
    String generate(@NotEmpty String firstName, @NotEmpty String lastName);
}
