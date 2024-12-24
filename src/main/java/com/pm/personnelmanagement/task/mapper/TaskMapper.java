package com.pm.personnelmanagement.task.mapper;

import com.pm.personnelmanagement.task.dto.TaskDTO;
import com.pm.personnelmanagement.task.model.Task;

public class TaskMapper {
    public static TaskDTO map(Task task) {
        return new TaskDTO(
                task.getUuid().toString(),
                task.getName(),
                task.getDescription(),
                task.getStartDateTime(),
                task.getEndDateTime(),
                task.getColor(),
                task.getCreatedBy(),
                task.getTaskEvent().getId()
        );
    }
}
