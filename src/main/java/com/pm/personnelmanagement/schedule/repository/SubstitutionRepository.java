package com.pm.personnelmanagement.schedule.repository;

import com.pm.personnelmanagement.schedule.model.Substitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubstitutionRepository extends JpaRepository<Substitution, Long>, JpaSpecificationExecutor<Substitution> {
    Optional<Substitution> findByUuid(UUID uuid);
}
