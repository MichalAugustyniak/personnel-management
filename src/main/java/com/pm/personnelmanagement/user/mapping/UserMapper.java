package com.pm.personnelmanagement.user.mapping;

import com.pm.personnelmanagement.user.dto.SimplifiedUserDTO;
import com.pm.personnelmanagement.user.dto.SimplifiedUsersResponse;
import com.pm.personnelmanagement.user.dto.UserDTO;
import com.pm.personnelmanagement.user.dto.UsersResponse;
import com.pm.personnelmanagement.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

public class UserMapper {
    public static UserDTO map(@NotNull User user) {
        return new UserDTO(
                user.getUuid(),
                user.getActive(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getRole().getName(),
                user.getSex(),
                user.getBirthday(),
                user.getUsername(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getAddress().getUuid(),
                user.getAdditionalAddress() == null ? null : user.getAdditionalAddress().getUuid(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public static UsersResponse map(@NotNull Page<User> users) {
        return new UsersResponse(
                users.getTotalElements(),
                users.getTotalPages(),
                users.getNumber(),
                users.getNumberOfElements(),
                users.getSize(),
                users.getContent().stream().map(UserMapper::map).toList()
        );
    }

    public static SimplifiedUserDTO mapToSimplified(@NotNull User user) {
        return new SimplifiedUserDTO(
                user.getUuid(),
                user.getUsername(),
                user.getLastLoginAt(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName()
        );
    }

    public static SimplifiedUsersResponse mapToSimplified(@NotNull Page<User> users) {
        return new SimplifiedUsersResponse(
                users.getTotalElements(),
                users.getTotalPages(),
                users.getNumber(),
                users.getNumberOfElements(),
                users.getSize(),
                users.getContent().stream().map(UserMapper::mapToSimplified).toList()
        );
    }
}
