package com.pm.personnelmanagement.user.repository;

import com.pm.personnelmanagement.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UUID uuid);

    Set<User> findAllByUuidIn(Collection<UUID> uuids);

    Set<User> findAllByUsernameIn(Collection<String> usernames);

    Optional<User> findByUsername(String username);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    boolean existsByUsername(String username);
}
