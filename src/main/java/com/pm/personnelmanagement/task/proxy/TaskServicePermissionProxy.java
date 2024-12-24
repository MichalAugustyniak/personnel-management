package com.pm.personnelmanagement.task.proxy;

import com.pm.personnelmanagement.security.repository.PermissionRepository;
import com.pm.personnelmanagement.task.dto.CreateTaskDTO;
import com.pm.personnelmanagement.task.dto.EditTaskDTO;
import com.pm.personnelmanagement.task.dto.TaskDTO;
import com.pm.personnelmanagement.task.dto.UserTaskRequest;
import com.pm.personnelmanagement.task.service.TaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

// todo: implement using Spring Security

@Service
public class TaskServicePermissionProxy implements TaskService {
    
    private final TaskService taskService;
    private final PermissionRepository permissionRepository;

    public TaskServicePermissionProxy(
            @Qualifier("defaultTaskService") TaskService taskService,
            PermissionRepository permissionRepository
    ) {
        this.taskService = taskService;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UUID createTask(CreateTaskDTO dto) {
        return null;
    }

    @Override
    public void editTask(EditTaskDTO dto) {

    }

    @Override
    public void deleteTask(long id) {

    }

    @Override
    public TaskDTO getTaskById(long id) {
        return null;
    }

    @Override
    public Page<TaskDTO> getAllTasks(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public void assignUserToTask(UserTaskRequest request, long taskId) {

    }

    @Override
    public void dismissUserFromTask(UserTaskRequest request, long taskId) {

    }
}
