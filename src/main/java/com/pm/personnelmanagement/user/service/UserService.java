package com.pm.personnelmanagement.user.service;

import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.user.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UserService {
    UserDTO getUser(AuthenticatedRequest<UserRequest> request);

    UsersResponse getUsers(AuthenticatedRequest<UsersRequest> request);

    SimplifiedUserDTO getSimplifiedUser(AuthenticatedRequest<UserRequest> request);

    SimplifiedUsersResponse getSimplifiedUsers(AuthenticatedRequest<UsersRequest> request);

    UserCreationResponse createUser(AuthenticatedRequest<UserCreationRequest> request);

    void updateUser(AuthenticatedRequest<UserEditionRequest> request);

    void deleteUser(AuthenticatedRequest<UserDeletionRequest> request);
}
