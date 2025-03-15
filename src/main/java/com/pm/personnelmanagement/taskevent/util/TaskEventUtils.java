package com.pm.personnelmanagement.taskevent.util;

import com.pm.personnelmanagement.taskevent.expcetion.TaskEventNotFoundException;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import com.pm.personnelmanagement.taskevent.repository.TaskEventRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskEventUtils {
    private final TaskEventRepository taskEventRepository;

    public TaskEventUtils(TaskEventRepository taskEventRepository) {
        this.taskEventRepository = taskEventRepository;
    }

    public TaskEvent fetchTaskEvent(@NotNull UUID uuid) {
        return taskEventRepository.findByUuid(uuid)
                .orElseThrow(() -> new TaskEventNotFoundException(
                        String.format("Task event of uuid %s not found", uuid)
                ));
    }

    public TaskEvent fetchTaskEventById(long id) {
        return taskEventRepository.findById(id)
                .orElseThrow(() -> new TaskEventNotFoundException(
                        String.format("Task event of id %s not found", id)
                ));
    }

    public Page<TaskEvent> fetchTaskEvents(Specification<TaskEvent> specification, int pageNumber, int pageSize) {
        return taskEventRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
    }
}
