package com.pm.personnelmanagement.taskevent.service;

import com.pm.personnelmanagement.taskevent.dto.EditTaskEventRequest;
import com.pm.personnelmanagement.taskevent.dto.TaskEventDTO;
import com.pm.personnelmanagement.taskevent.dto.TaskEventRequest;
import com.pm.personnelmanagement.taskevent.dto.TaskEventsRequest;
import com.pm.personnelmanagement.taskevent.expcetion.TaskEventNotFoundException;
import com.pm.personnelmanagement.taskevent.mapper.TaskEventMapper;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import com.pm.personnelmanagement.taskevent.repository.TaskEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultTaskEventService implements TaskEventService {

    private final TaskEventRepository taskEventRepository;

    public DefaultTaskEventService(TaskEventRepository taskEventRepository) {
        this.taskEventRepository = taskEventRepository;
    }

    @Override
    public void createTaskEvent(TaskEventRequest request) {
        TaskEvent taskEvent = new TaskEvent();
        System.out.println(request);
        taskEvent.setName(request.name());
        taskEvent.setDescription(request.description());
        taskEvent.setStartDateTime(request.startDateTime());
        taskEvent.setEndDateTime(request.endDateTime());
        taskEventRepository.save(taskEvent);
    }

    @Override
    public void editTaskEvent(EditTaskEventRequest request) {
        TaskEvent taskEvent = taskEventRepository.findTaskEventById(request.id())
                .orElseThrow(
                        () -> new TaskEventNotFoundException(String.format("Task event of id %d not found", request.id()))
                );
        request.taskEvent().name().ifPresent(taskEvent::setName);
        request.taskEvent().description().ifPresent(taskEvent::setDescription);
        request.taskEvent().startDateTime().ifPresent(taskEvent::setStartDateTime);
        request.taskEvent().endDateTime().ifPresent(taskEvent::setEndDateTime);
        taskEventRepository.save(taskEvent);
    }

    @Override
    public void deleteTaskEvent(long id) {
        taskEventRepository.findTaskEventById(id)
                .orElseThrow(
                        () -> new TaskEventNotFoundException(String.format("Task event of id %d not found", id))
                );
        taskEventRepository.removeTaskEventById(id);
    }

    @Override
    public TaskEventDTO getTaskEventById(long id) {
        TaskEvent taskEvent = taskEventRepository.findTaskEventById(id)
                .orElseThrow(
                        () -> new TaskEventNotFoundException(String.format("Task event of id %s not found", id))
                );
        return TaskEventMapper.map(taskEvent);
    }

    @Override
    public Page<TaskEventDTO> getAllTaskEvents(TaskEventsRequest request) {
        return taskEventRepository.findAll(PageRequest.of(request.pageNumber(), request.pageSize()))
                .map(TaskEventMapper::map);
    }
}
