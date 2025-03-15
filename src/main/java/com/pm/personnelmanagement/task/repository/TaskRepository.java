package com.pm.personnelmanagement.task.repository;

import com.pm.personnelmanagement.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskById(Long id);

    Optional<Task> findByUuid(UUID uuid);

    Page<Task> findAll(Specification<Task> specification, Pageable pageable);
}
