package com.pm.personnelmanagement.user.util;

import com.pm.personnelmanagement.user.exception.UserNotFoundException;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserUtils {
    private final UserRepository userRepository;

    public UserUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User fetchUser(@NotNull UUID uuid) {
        return userRepository.findByUuid(uuid).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User of uuid %s not found", uuid)
                ));
    }
}
