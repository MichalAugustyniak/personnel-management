package com.pm.personnelmanagement.task.controller;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.task.dto.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskController {
    ResponseEntity<TaskCreationResponse> createTask(TaskCreationRequest request);

    ResponseEntity<Void> editTask(TaskUpdateRuquestBody request, UUID taskUUID);

    ResponseEntity<Void> deleteTask(UUID taskUUID);

    ResponseEntity<TaskDTO> getTaskById(UUID uuid);

    ResponseEntity<PagedResponse<TaskDTO>> getAllTasks(Integer pageNumber,
                                                       Integer pageSize,
                                                       String nameLike,
                                                       String createdBy,
                                                       String user,
                                                       UUID taskEventUUID,
                                                       LocalDateTime from,
                                                       LocalDateTime to);

    ResponseEntity<Void> assignUserToTask(UserTaskRequest request, UUID taskUUID);

    ResponseEntity<Void> dismissUserFromTask(UserTaskRequest request, UUID taskUUID);

    ResponseEntity<TaskUsers> getUsersByTask(UUID uuid);
}
