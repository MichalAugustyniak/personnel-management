package com.pm.personnelmanagement.task.util;

import com.pm.personnelmanagement.task.exception.TaskNotFoundException;
import com.pm.personnelmanagement.task.model.Task;
import com.pm.personnelmanagement.task.repository.TaskRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class TaskUtils {
    private final TaskRepository taskRepository;

    public TaskUtils(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task fetchTask(@NotNull UUID uuid) {
        return taskRepository.findByUuid(uuid)
                .orElseThrow(() -> new TaskNotFoundException(
                        String.format("Task of uuid %s not found", uuid)
                ));
    }

    public List<Task> fetchByIds(Set<Long> ids) {
        return taskRepository.findAllById(ids);
    }

    public Task fetchTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                    String.format("Task of id %s not found", id)
                ));
    }
}
