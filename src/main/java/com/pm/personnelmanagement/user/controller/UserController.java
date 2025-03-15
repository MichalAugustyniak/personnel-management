package com.pm.personnelmanagement.user.controller;

import com.pm.personnelmanagement.user.dto.UserCreationRequest;
import com.pm.personnelmanagement.user.dto.UserCreationResponse;
import com.pm.personnelmanagement.user.dto.UserEditionRequestBody;
import com.pm.personnelmanagement.user.model.Sex;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserController {
    ResponseEntity<Object> getUser(UUID uuid, String type);

    ResponseEntity<Object> getUsers(String role,
                                    UUID addressUUID,
                                    Sex sex,
                                    Boolean isActive,
                                    String like,
                                    int pageNumber,
                                    int pageSize,
                                    String type);

    ResponseEntity<UserCreationResponse> createUser(UserCreationRequest request);

    ResponseEntity<Void> updateUser(UUID uuid, UserEditionRequestBody body);

    ResponseEntity<Void> deleteUser(UUID uuid);
}
