package com.pm.personnelmanagement.taskevent.repository;

import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskEventRepository extends JpaRepository<TaskEvent, Long> {
    Optional<TaskEvent> findTaskEventById(Long id);
}
