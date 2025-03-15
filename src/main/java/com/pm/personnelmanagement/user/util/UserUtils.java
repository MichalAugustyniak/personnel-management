package com.pm.personnelmanagement.user.util;

import com.pm.personnelmanagement.user.exception.UserNotFoundException;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    public User fetchUserByUsername(@NotEmpty String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(
                        String.format("User %s not found", username)
                ));
    }

    public boolean existById(long id) {
        return userRepository.existsById(id);
    }

    public Page<User> fetchUsers(Specification<User> specification, int pageNumber, int pageSize) {
        return userRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
    }
}
