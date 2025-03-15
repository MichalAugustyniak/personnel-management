package com.pm.personnelmanagement.user.component;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DefaultUsernameGenerator implements UsernameGenerator {
    @Override
    public String generate(String firstName, String lastName) {
        Random random = new Random();
        return firstName + lastName + random.nextInt(100);
    }
}
