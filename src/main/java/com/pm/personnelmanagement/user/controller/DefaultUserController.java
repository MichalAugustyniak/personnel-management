package com.pm.personnelmanagement.user.controller;

import com.pm.personnelmanagement.permission.exception.UnauthorizedException;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.dto.*;
import com.pm.personnelmanagement.user.exception.InvalidValueException;
import com.pm.personnelmanagement.user.model.Sex;
import com.pm.personnelmanagement.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class DefaultUserController  {
    private final UserService userService;

    public DefaultUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{uuid}")
    //@Override
    public ResponseEntity<Object> getUser(@NotNull @PathVariable UUID uuid, @NotEmpty @RequestParam(defaultValue = "simplified") String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthenticatedRequest<UserRequest> request = new AuthenticatedRequest<>(authentication.getName(), new UserRequest(uuid));
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        boolean isAdminOrHR = authorities.contains("ROLE_" + DefaultRoleNames.ADMIN) || authorities.contains("ROLE_" + DefaultRoleNames.HR);
        if (type.equals("full") && !isAdminOrHR) {
            throw new UnauthorizedException("You have no access to do that");
        }
        if (type.equals("simplified")) {
            return new ResponseEntity<>(
                    userService.getSimplifiedUser(request),
                    HttpStatus.OK
            );
        } else if (type.equals("full")) {
            return new ResponseEntity<>(
                    userService.getUser(request),
                    HttpStatus.OK
            );
        } else {
            throw new InvalidValueException("Bad 'type' argument");
        }
    }

    @GetMapping
    //@Override
    public ResponseEntity<Object> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) UUID addressUUID,
            @RequestParam(required = false) Sex sex,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String like,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50") int pageSize,
            @RequestParam(defaultValue = "simplified") String type) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        boolean isAdminOrHR = authorities.contains("ROLE_" + DefaultRoleNames.ADMIN) || authorities.contains("ROLE_" + DefaultRoleNames.HR);
        System.out.println(authorities);
        UsersRequest usersRequest = new UsersRequest(role, addressUUID, sex, isActive, like, pageNumber, pageSize);
        if (type.equals("full") && !isAdminOrHR) {
            throw new UnauthorizedException("You have no access to do that");
        }
        if (type.equals("simplified")) {
            return new ResponseEntity<>(
                    userService.getSimplifiedUsers(new AuthenticatedRequest<>(
                            authentication.getName(),
                            usersRequest
                    )),
                    HttpStatus.OK
            );
        } else if (type.equals("full")) {
            return new ResponseEntity<>(
                    userService.getUsers(new AuthenticatedRequest<>(
                            authentication.getName(),
                            usersRequest
                    )),
                    HttpStatus.OK
            );
        } else {
            throw new InvalidValueException("Bad 'type' argument");
        }
    }

    @PostMapping
    //@Override
    public ResponseEntity<UserCreationResponse> createUser(@NotNull @Valid @RequestBody UserCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(userService.createUser(
                new AuthenticatedRequest<>(authentication.getName(), request)),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{uuid}")
    //@Override
    public ResponseEntity<Void> updateUser(@NotNull @PathVariable UUID uuid, @NotNull @Valid @RequestBody UserEditionRequestBody body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.updateUser(
                new AuthenticatedRequest<>(authentication.getName(), new UserEditionRequest(
                        uuid, body
                )));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    //@Override
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable UUID uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteUser(new AuthenticatedRequest<>(authentication.getName(), new UserDeletionRequest(uuid)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
