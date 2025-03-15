package com.pm.personnelmanagement.taskevent.service;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.taskevent.dto.*;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TaskEventService {
    TaskEventCreationResponse createTaskEvent(AuthenticatedRequest<TaskEventCreationRequest> request);

    void editTaskEvent(AuthenticatedRequest<TaskEventUpdateRequest> request);

    void deleteTaskEvent(AuthenticatedRequest<TaskEventDeletionRequest> request);

    TaskEventDTO getTaskEvent(AuthenticatedRequest<TaskEventRequest> request);

    PagedResponse<TaskEventDTO> getAllTaskEvents(AuthenticatedRequest<TaskEventsRequest> request);
}
