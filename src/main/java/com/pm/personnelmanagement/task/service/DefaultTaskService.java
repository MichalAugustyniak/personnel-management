package com.pm.personnelmanagement.task.service;

import com.pm.personnelmanagement.task.dto.*;
import com.pm.personnelmanagement.task.exception.TaskNotFoundException;
import com.pm.personnelmanagement.task.mapper.TaskMapper;
import com.pm.personnelmanagement.task.model.Task;
import com.pm.personnelmanagement.task.repository.TaskRepository;
import com.pm.personnelmanagement.taskevent.expcetion.TaskEventNotFoundException;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import com.pm.personnelmanagement.taskevent.repository.TaskEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultTaskService implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventRepository taskEventRepository;

    public DefaultTaskService(TaskRepository taskRepository, TaskEventRepository taskEventRepository) {
        this.taskRepository = taskRepository;
        this.taskEventRepository = taskEventRepository;
    }

    @Override
    public UUID createTask(CreateTaskDTO dto) {
        TaskEvent taskEvent = taskEventRepository.findTaskEventById(dto.task().taskEventId())
                .orElseThrow(
                        () -> new TaskEventNotFoundException(String.format("Task event of id %d not found", dto.task().taskEventId()))
                );
        Task task = new Task();
        task.setName(dto.task().name());
        task.setDescription(dto.task().description());
        task.setStartDateTime(dto.task().startDateTime());
        task.setEndDateTime(dto.task().endDateTime());
        task.setColor(dto.task().color());
        task.setCreatedBy(dto.createdBy());
        task.setTaskEvent(taskEvent);
        taskRepository.save(task);
        return task.getUuid();
    }

    @Override
    public void editTask(EditTaskDTO dto) {
        Task task = taskRepository.findTaskById(dto.id())
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task of id %d not found", dto.id())));
        dto.task().taskEventId().ifPresent(taskEventId -> {
            TaskEvent taskEvent = taskEventRepository.findTaskEventById(taskEventId)
                    .orElseThrow(() -> new TaskEventNotFoundException(String.format("Task event of id %d not found", taskEventId)));
            task.setTaskEvent(taskEvent);
        });
        dto.task().name().ifPresent(task::setName);
        dto.task().description().ifPresent(task::setDescription);
        dto.task().startDateTime().ifPresent(task::setStartDateTime);
        dto.task().endDateTime().ifPresent(task::setEndDateTime);
        dto.task().color().ifPresent(task::setColor);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(long id) {
        taskRepository.findTaskById(id).orElseThrow(() -> new TaskEventNotFoundException(String.format("Task of id %d not found", id)));
        taskRepository.deleteById(id);
    }

    @Override
    public TaskDTO getTaskById(long id) {
        Task task = taskRepository.findTaskById(id).orElseThrow(
                () -> new TaskEventNotFoundException(String.format("Task of id %d not found", id))
        );
        return TaskMapper.map(task);
    }

    @Override
    public Page<TaskDTO> getAllTasks(int pageNumber, int pageSize) {
        return taskRepository.findAll(PageRequest.of(pageNumber, pageSize)).map(TaskMapper::map);
    }

    @Override
    public void assignUserToTask(UserTaskRequest request, long taskId) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task of id %d not found", taskId)));
        // todo when auth is set: check if user exists
        task.getUsers().add(request.username());
        taskRepository.save(task);
    }

    @Override
    public void dismissUserFromTask(UserTaskRequest request, long taskId) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(String.format("Task of id %d not found", taskId)));
        // todo when auth is set: check if user exists
        task.getUsers().removeIf(request.username()::equals);
        taskRepository.save(task);
    }
}
