package com.pm.personnelmanagement.taskevent.controller;

import com.pm.personnelmanagement.taskevent.dto.TaskEventDTO;
import com.pm.personnelmanagement.taskevent.dto.TaskEventEditRequest;
import com.pm.personnelmanagement.taskevent.dto.TaskEventRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface TaskEventController {
    ResponseEntity<Void> createTaskEvent(TaskEventRequest request);

    ResponseEntity<Void> editTaskEvent(long id, TaskEventEditRequest request);

    ResponseEntity<Void> deleteTaskEvent(long id);

    ResponseEntity<TaskEventDTO> getTaskEventById(long id);

    ResponseEntity<Page<TaskEventDTO>> getAllTaskEvents(int pageNumber, int pageSize);
}
