package com.pm.personnelmanagement.task.service;

import com.pm.personnelmanagement.task.dto.*;

import java.util.UUID;

public interface PermissionedTaskService {
    UUID createTask(PermissionedTaskCreationRequest request);

    void editTask(TaskUpdateRequest dto);

    void deleteTask(UUID uuid);

    TaskDTO getTask(UUID uuid);

    TasksResponse getAllTasks(TasksRequestFilters filters);

    void assignUserToTask(AssignUserToTaskRequest request);

    void dismissUserFromTask(DismissUserFromTaskRequest request);
}
