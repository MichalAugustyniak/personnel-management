package com.pm.personnelmanagement.job.repository;

import com.pm.personnelmanagement.job.model.Contract;
import com.pm.personnelmanagement.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByUser(User user);

    Optional<Contract> findByUuid(UUID uuid);
}
