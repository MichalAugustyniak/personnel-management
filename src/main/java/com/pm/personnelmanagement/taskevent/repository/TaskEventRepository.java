package com.pm.personnelmanagement.taskevent.repository;

import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import com.pm.personnelmanagement.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskEventRepository extends JpaRepository<TaskEvent, Long>, JpaSpecificationExecutor<TaskEvent> {
    Optional<TaskEvent> findTaskEventById(Long id);

    Optional<TaskEvent> findByUuid(UUID uuid);

    @Query("SELECT CASE WHEN COUNT(te) > 0 THEN true ELSE false END " +
            "FROM TaskEvent te " +
            "JOIN te.tasks t " +
            "JOIN t.users u " +
            "JOIN te.createdBy author " +
            "WHERE (u = :user OR author = :user) " +
            "AND te = :taskEvent")
    boolean hasUser(@Param("taskEvent") TaskEvent taskEvent, @Param("user") User user);

}
