package com.pm.personnelmanagement.taskevent.controller;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.taskevent.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TaskEventController {
    ResponseEntity<TaskEventCreationResponse> createTaskEvent(TaskEventCreationRequest request);

    ResponseEntity<Void> editTaskEvent(UUID uuid, TaskEventUpdateRequestBody request);

    ResponseEntity<Void> deleteTaskEvent(UUID uuid);

    ResponseEntity<TaskEventDTO> getTaskEventById(UUID uuid);

    ResponseEntity<PagedResponse<TaskEventDTO>> getAllTaskEvents(String like,
                                                                 LocalDateTime from,
                                                                 LocalDateTime to,
                                                                 String user,
                                                                 String createdBy,
                                                                 int pageNumber,
                                                                 int pageSize);
}
