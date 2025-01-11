package com.pm.personnelmanagement.common;

import java.util.Optional;

public class NullChecker {
    public static void validate(Object object, String messagePrefix) {
        if (Optional.ofNullable(object).isEmpty()) {
            throw new IllegalArgumentException(String.format("%s cannot be null", messagePrefix));
        }
    }
}
