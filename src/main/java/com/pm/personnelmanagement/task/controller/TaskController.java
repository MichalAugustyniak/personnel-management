package com.pm.personnelmanagement.task.controller;

import com.pm.personnelmanagement.task.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface TaskController {
    ResponseEntity<Void> createTask(CreateTaskRequest request, HttpServletRequest httpServletRequest);

    ResponseEntity<Void> editTask(EditTaskRequest request, long id, HttpServletRequest httpServletRequest);

    ResponseEntity<Void> deleteTask(long id, HttpServletRequest httpServletRequest);

    ResponseEntity<TaskDTO> getTaskById(long id);

    ResponseEntity<Page<TaskDTO>> getAllTasks(int pageNumber, int pageSize);

    ResponseEntity<Void> assignUserToTask(UserTaskRequest request, long taskId);

    ResponseEntity<Void> dismissUserFromTask(UserTaskRequest request, long taskId);
}
