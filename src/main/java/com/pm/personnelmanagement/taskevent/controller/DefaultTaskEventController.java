package com.pm.personnelmanagement.taskevent.controller;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.taskevent.dto.*;
import com.pm.personnelmanagement.taskevent.service.TaskEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/task-events")
public class DefaultTaskEventController implements TaskEventController {
    private final TaskEventService taskEventService;

    public DefaultTaskEventController(TaskEventService taskEventService) {
        this.taskEventService = taskEventService;
    }

    @Override
    @PostMapping
    public ResponseEntity<TaskEventCreationResponse> createTaskEvent(@RequestBody TaskEventCreationRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return new ResponseEntity<>(taskEventService.createTaskEvent(
                new AuthenticatedRequest<>(
                        securityContext.getAuthentication().getName(),
                        request
                )
        ), HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{uuid}")
    public ResponseEntity<Void> editTaskEvent(
            @PathVariable UUID uuid,
            @RequestBody TaskEventUpdateRequestBody request
    ) {
        SecurityContext context = SecurityContextHolder.getContext();
        taskEventService.editTaskEvent(new AuthenticatedRequest<>(
                context.getAuthentication().getName(),
                new TaskEventUpdateRequest(uuid, request)
        ));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteTaskEvent(@PathVariable UUID uuid) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        taskEventService.deleteTaskEvent(new AuthenticatedRequest<>(
                securityContext.getAuthentication().getName(),
                new TaskEventDeletionRequest(uuid)
        ));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{uuid}")
    public ResponseEntity<TaskEventDTO> getTaskEventById(@PathVariable UUID uuid) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return ResponseEntity.ok(taskEventService.getTaskEvent(
                new AuthenticatedRequest<>(
                        securityContext.getAuthentication().getName(),
                        new TaskEventRequest(uuid)
                )
        ));
    }

    @Override
    @GetMapping
    public ResponseEntity<PagedResponse<TaskEventDTO>> getAllTaskEvents(
            @RequestParam(required = false) String like,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) String createdBy,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "50")  int pageSize
    ) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return ResponseEntity.ok(taskEventService.getAllTaskEvents(
                new AuthenticatedRequest<>(
                        securityContext.getAuthentication().getName(),
                        new TaskEventsRequest(
                                like,
                                from,
                                to,
                                user,
                                createdBy,
                                pageNumber,
                                pageSize
                        )
                )
        ));
    }
}
