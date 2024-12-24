package com.pm.personnelmanagement.taskevent.service;

import com.pm.personnelmanagement.taskevent.dto.EditTaskEventRequest;
import com.pm.personnelmanagement.taskevent.dto.TaskEventDTO;
import com.pm.personnelmanagement.taskevent.dto.TaskEventRequest;
import com.pm.personnelmanagement.taskevent.dto.TaskEventsRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TaskEventService {
    UUID createTaskEvent(TaskEventRequest request);

    void editTaskEvent(EditTaskEventRequest request);

    void deleteTaskEvent(long id);

    TaskEventDTO getTaskEventById(long id);

    Page<TaskEventDTO> getAllTaskEvents(TaskEventsRequest request);
}
