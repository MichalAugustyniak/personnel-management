package com.pm.personnelmanagement.user.service;

import com.pm.personnelmanagement.common.util.SpecificationUtils;
import com.pm.personnelmanagement.file.exception.NotImplementedException;
import com.pm.personnelmanagement.permission.constant.ResourceTypes;
import com.pm.personnelmanagement.permission.exception.UnauthorizedException;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.user.component.UsernameGenerator;
import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.dto.*;
import com.pm.personnelmanagement.user.exception.InvalidValueException;
import com.pm.personnelmanagement.user.mapping.UserMapper;
import com.pm.personnelmanagement.user.model.Address;
import com.pm.personnelmanagement.user.model.Role;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import com.pm.personnelmanagement.user.util.AddressUtils;
import com.pm.personnelmanagement.user.util.RoleUtils;
import com.pm.personnelmanagement.user.util.UserUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Validated
@Service
public class DefaultUserService implements UserService {
    private final static String CONTEXT_TYPE = ResourceTypes.USER;
    private final UserUtils userUtils;
    private final RoleUtils roleUtils;
    private final AddressUtils addressUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UsernameGenerator usernameGenerator;

    public DefaultUserService(UserUtils userUtils, RoleUtils roleUtils, AddressUtils addressUtils, PasswordEncoder passwordEncoder, UserRepository userRepository, UsernameGenerator usernameGenerator) {
        this.userUtils = userUtils;
        this.roleUtils = roleUtils;
        this.addressUtils = addressUtils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.usernameGenerator = usernameGenerator;
    }

    @Override
    public UserDTO getUser(AuthenticatedRequest<UserRequest> request) {
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        User searchedUser = userUtils.fetchUser(request.request().uuid());
        boolean hasAccessByRole = principalUser.getRole().getName().equals(DefaultRoleNames.ADMIN) || principalUser.getRole().getName().equals(DefaultRoleNames.HR);
        boolean isSearchingItself = principalUser.equals(searchedUser);
        if (!hasAccessByRole && !isSearchingItself) {
            throw new UnauthorizedException("You have no access to do that");
        }
        return UserMapper.map(searchedUser);
    }

    @Override
    public UsersResponse getUsers(AuthenticatedRequest<UsersRequest> request) {
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        boolean hasAccessByRole = principalUser.getRole().getName().equals(DefaultRoleNames.ADMIN) || principalUser.getRole().getName().equals(DefaultRoleNames.HR);
        if (!hasAccessByRole) {
            throw new UnauthorizedException("You have no access to do that");
        }
        Specification<User> specification = userSpecification(request);
        Page<User> users = userUtils.fetchUsers(specification, request.request().pageNumber(), request.request().pageSize());
        return UserMapper.map(users);
    }

    @Override
    public SimplifiedUserDTO getSimplifiedUser(AuthenticatedRequest<UserRequest> request) {
        User user = userUtils.fetchUser(request.request().uuid());
        return UserMapper.mapToSimplified(user);
    }

    @Override
    public SimplifiedUsersResponse getSimplifiedUsers(AuthenticatedRequest<UsersRequest> request) {
        Specification<User> specification = userSpecification(request);
        Page<User> users = userUtils.fetchUsers(specification, request.request().pageNumber(), request.request().pageSize());
        return UserMapper.mapToSimplified(users);
    }

    @Override
    public UserCreationResponse createUser(AuthenticatedRequest<UserCreationRequest> request) {
        Role role = roleUtils.fetchRoleByName(request.request().role());
        Address address = addressUtils.fetchByUUID(request.request().addressUUID());
        Address additionalAddress = null;
        if (request.request().additionalAddressUUID() != null) {
            additionalAddress = addressUtils.fetchByUUID(request.request().additionalAddressUUID());
        }
        UUID uuid = UUID.randomUUID();
        User user = new User();
        String username = request.request().username();
        boolean isUsernameNotNull = Optional.ofNullable(username).isPresent();
        if (isUsernameNotNull && userRepository.existsByUsername(username)) {
            throw new InvalidValueException("The username is already in use");
        }
        user.setRole(role);
        user.setCreatedAt(LocalDateTime.now());
        user.setBirthday(request.request().birthday());
        user.setSex(request.request().sex());
        user.setAddress(address);
        user.setEmail(request.request().email());
        user.setPhone(request.request().phone());
        user.setAdditionalAddress(additionalAddress);
        user.setFirstName(request.request().firstName());
        user.setMiddleName(request.request().middleName());
        user.setLastName(request.request().lastName());
        user.setUuid(uuid);
        user.setActive(request.request().isActive());
        if (isUsernameNotNull && username.length() > 4) {
            user.setUsername(username);
        } else if (isUsernameNotNull && username.length() < 4) {
            throw new InvalidValueException("Username length has to be longer than 3 characters");
        } else {
            String generatedUsername = usernameGenerator.generate(request.request().firstName(), request.request().lastName());
            while (userRepository.existsByUsername(generatedUsername)) {
                generatedUsername = usernameGenerator.generate(request.request().firstName(), request.request().lastName());
            }
            user.setUsername(generatedUsername);
        }
        user.setPassword(passwordEncoder.encode(request.request().password()));
        userRepository.save(user);
        return new UserCreationResponse(uuid);
    }

    @Override
    public void updateUser(AuthenticatedRequest<UserEditionRequest> request) {
        UserEditionRequestBody body = request.request().request();
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        User updatedUser = userUtils.fetchUser(request.request().uuid());
        Optional.ofNullable(body.additionalAddressUUID()).ifPresent(additionalAddressUUID -> {
            Address additionalAddress = addressUtils.fetchByUUID(additionalAddressUUID);
            updateByRole(principalUser, updatedUser, additionalAddress, updatedUser::setAdditionalAddress);
        });
        boolean isUsernameNotNull = Optional.ofNullable(body.username()).isPresent();
        if (isUsernameNotNull && (!updatedUser.getUsername().equals(body.username())) && userRepository.existsByUsername(body.username()) && !principalUser.getUsername().equals(body.username())) {
            throw new InvalidValueException("The username is already in use");
        }
        Optional.ofNullable(body.password()).ifPresent(password -> updateByUserOrRole(principalUser, updatedUser, passwordEncoder.encode(body.password()), updatedUser::setPassword));
        updateByRole(principalUser, updatedUser, body.isActive(), updatedUser::setActive);
        Optional.ofNullable(body.role()).ifPresent(uuid -> {
            Role role = roleUtils.fetchRoleByName(body.role());
            updateByRole(principalUser, updatedUser, role, updatedUser::setRole);
        });
        Optional.ofNullable(body.addressUUID()).ifPresent(uuid -> {
            Address address = addressUtils.fetchByUUID(uuid);
            updateByRole(principalUser, updatedUser, address, updatedUser::setAddress);
        });
        updateByRole(principalUser, updatedUser, body.birthday(), updatedUser::setBirthday);
        updateByRole(principalUser, updatedUser, body.email(), updatedUser::setEmail);
        updateByRole(principalUser, updatedUser, body.firstName(), updatedUser::setFirstName);
        updateByRole(principalUser, updatedUser, body.middleName(), updatedUser::setMiddleName);
        updateByRole(principalUser, updatedUser, body.lastName(), updatedUser::setLastName);
        updateByRole(principalUser, updatedUser, body.phone(), updatedUser::setPhone);
        updateByRole(principalUser, updatedUser, body.sex(), updatedUser::setSex);
        updateByRole(principalUser, updatedUser, body.username(), updatedUser::setUsername);
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(AuthenticatedRequest<UserDeletionRequest> request) {
        throw new NotImplementedException("Cannot delete user");
    }

    private Specification<User> userSpecification(AuthenticatedRequest<UsersRequest> request) {
        UsersRequest userRequest = request.request();
        Specification<User> specification = Specification.where(null);
        specification = SpecificationUtils.addCriteria(specification, userRequest.sex(), "sex");
        specification = SpecificationUtils.addCriteria(specification, userRequest.isActive(), "isActive");
        if (userRequest.addressUUID() != null) {
            Specification<User> hasAddress = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("address").get("uuid"), userRequest.addressUUID());
            specification = specification.and(hasAddress);
        }
        if (userRequest.role() != null) {
            Specification<User> hasRole = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("role").get("name"), userRequest.role());
            specification = specification.and(hasRole);
        }
        if (userRequest.like() != null) {
            Specification<User> hasLikePattern = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + userRequest.like().toLowerCase() + "%";
                Predicate usernamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        likePattern
                );
                Predicate fullNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(
                                criteriaBuilder.concat(
                                        criteriaBuilder.concat(
                                                root.get("firstName"), root.get("middleName")),
                                        root.get("lastName")
                                )
                        ), likePattern);
                return criteriaBuilder.or(usernamePredicate, fullNamePredicate);
            };
            specification = specification.and(hasLikePattern);
        }
        /*
        Optional.ofNullable(request.request().addressUUID()).ifPresent(addressUUID -> {
            Specification<User> hasAddress = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("address").get("uuid"), addressUUID);
            specification.and(hasAddress);
        });
        Optional.ofNullable(request.request().sex()).ifPresent(sex -> {
            Specification<User> hasSex = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("sex"), sex);
            specification.and(hasSex);
        });
        Optional.ofNullable(request.request().isActive()).ifPresent(isActive -> {
            Specification<User> hasActiveStatus = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isActive"), isActive);
            specification.and(hasActiveStatus);
        });
        Optional.ofNullable(request.request().role()).ifPresent(role -> {
            Specification<User> hasRole = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("role").get("name"), role);
            specification.and(hasRole);
        });
        Optional.ofNullable(request.request().like()).ifPresent(like -> {
            Specification<User> hasLikePattern = (root, query, criteriaBuilder) -> {
                String likePattern = "%" + like.toLowerCase() + "%";
                Predicate usernamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("username")),
                        likePattern
                );
                Predicate fullNamePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(
                                criteriaBuilder.concat(
                                        criteriaBuilder.concat(
                                                root.get("firstName"), root.get("middleName")),
                                        root.get("lastName")
                                )
                        ), likePattern);
                return criteriaBuilder.or(usernamePredicate, fullNamePredicate);
            };
            specification.and(hasLikePattern);
        });

         */
        return specification;
    }

    private <T> void updateByUserOrRole(User principalUser, User updatedUser, T propertyValue, Consumer<T> setter) {
        boolean canAccessByRole = principalUser.getRole().getName().equals(DefaultRoleNames.ADMIN) || principalUser.getRole().getName().equals(DefaultRoleNames.HR);
        boolean isUpdatingItself = updatedUser.equals(principalUser);
        boolean isPropertyPresent = Optional.ofNullable(propertyValue).isPresent();
        if (!isPropertyPresent) {
            return;
        }
        if (!(isUpdatingItself || canAccessByRole)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        setter.accept(propertyValue);
    }

    private <T> void updateByRole(User principalUser, User updatedUser, T propertyValue, Consumer<T> setter) {
        boolean canAccessByRole = principalUser.getRole().getName().equals(DefaultRoleNames.ADMIN) || principalUser.getRole().getName().equals(DefaultRoleNames.HR);
        boolean isPropertyPresent = Optional.ofNullable(propertyValue).isPresent();
        if (!isPropertyPresent) {
            return;
        }
        if (!canAccessByRole) {
            throw new UnauthorizedException("You have no access to do that");
        }
        setter.accept(propertyValue);
    }
}
