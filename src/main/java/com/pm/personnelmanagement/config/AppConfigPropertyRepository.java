package com.pm.personnelmanagement.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppConfigPropertyRepository extends JpaRepository<AppConfigProperty, Long> {
    Optional<AppConfigProperty> findByPropertyName(String propertyName);

    boolean existsByPropertyName(String propertyName);
}
