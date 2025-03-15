package com.pm.personnelmanagement.task.service;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.task.dto.*;

public interface TaskService {
    TaskCreationResponse createTask(AuthenticatedRequest<TaskCreationRequest> request);

    void editTask(AuthenticatedRequest<TaskUpdateRequest> request);

    void deleteTask(AuthenticatedRequest<TaskDeletionRequest> request);

    TaskDTO getTask(AuthenticatedRequest<TaskRequest> request);

    PagedResponse<TaskDTO> getAllTasks(AuthenticatedRequest<TasksRequestFilters> request);

    void assignUserToTask(AuthenticatedRequest<AssignUserToTaskRequest> request);

    void dismissUserFromTask(AuthenticatedRequest<DismissUserFromTaskRequest> request);

    TaskUsers getUsersByTask(TaskUsersRequest request);
}
