package com.pm.personnelmanagement.task.service;

import com.pm.personnelmanagement.task.dto.*;
import org.springframework.data.domain.Page;

public interface TaskService {
    void createTask(CreateTaskDTO dto);

    void editTask(EditTaskDTO dto);

    void deleteTask(long id);

    TaskDTO getTaskById(long id);

    Page<TaskDTO> getAllTasks(int pageNumber, int pageSize);

    void assignUserToTask(UserTaskRequest request, long taskId);

    void dismissUserFromTask(UserTaskRequest request, long taskId);
}
