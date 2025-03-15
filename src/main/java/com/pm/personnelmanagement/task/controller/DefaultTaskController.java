package com.pm.personnelmanagement.task.controller;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.task.dto.*;
import com.pm.personnelmanagement.task.service.TaskService;
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
@RequestMapping("/api/tasks")
public class DefaultTaskController implements TaskController {
    private final TaskService taskService;

    public DefaultTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @PostMapping
    public ResponseEntity<TaskCreationResponse> createTask(@RequestBody TaskCreationRequest request) {
        SecurityContext context = SecurityContextHolder.getContext();
        TaskCreationResponse response = taskService.createTask(new AuthenticatedRequest<>(
                context.getAuthentication().getName(), request
        ));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{taskUUID}")
    public ResponseEntity<Void> editTask(@RequestBody TaskUpdateRuquestBody request, @PathVariable UUID taskUUID) {
        System.out.println(taskUUID);
        SecurityContext context = SecurityContextHolder.getContext();
        taskService.editTask(new AuthenticatedRequest<>(context.getAuthentication().getName(), new TaskUpdateRequest(taskUUID, request)));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{taskUUID}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskUUID) {
        SecurityContext context = SecurityContextHolder.getContext();
        taskService.deleteTask(new AuthenticatedRequest<>(context.getAuthentication().getName(), new TaskDeletionRequest(taskUUID)));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{taskUUID}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID taskUUID) {
        SecurityContext context = SecurityContextHolder.getContext();
        return ResponseEntity.ok(taskService.getTask(
                new AuthenticatedRequest<>(context.getAuthentication().getName(), new TaskRequest(taskUUID))
        ));
    }

    @Override
    @GetMapping
    public ResponseEntity<PagedResponse<TaskDTO>> getAllTasks(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String nameLike,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) UUID taskEventUUID,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to
            ) {
        SecurityContext context = SecurityContextHolder.getContext();
        return ResponseEntity.ok(taskService.getAllTasks(
                new AuthenticatedRequest<>(
                        context.getAuthentication().getName(),
                        new TasksRequestFilters(pageNumber, pageSize, nameLike, createdBy, user, taskEventUUID, from, to)
                )
        ));
    }

    @Override
    @PostMapping("/{taskUUID}/users")
    public ResponseEntity<Void> assignUserToTask(@RequestBody UserTaskRequest request, @PathVariable UUID taskUUID) {
        SecurityContext context = SecurityContextHolder.getContext();
        taskService.assignUserToTask(new AuthenticatedRequest<>(context.getAuthentication().getName(), new AssignUserToTaskRequest(request.userUUID(), taskUUID)));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{taskUUID}/users")
    public ResponseEntity<Void> dismissUserFromTask(@RequestBody UserTaskRequest request, @PathVariable UUID taskUUID) {
        SecurityContext context = SecurityContextHolder.getContext();
        taskService.dismissUserFromTask(new AuthenticatedRequest<>(
                context.getAuthentication().getName(),
                new DismissUserFromTaskRequest(request.userUUID(), taskUUID)
        ));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{taskUUID}/users")
    public ResponseEntity<TaskUsers> getUsersByTask(@PathVariable UUID taskUUID) {
        return ResponseEntity.ok(
                taskService.getUsersByTask(new TaskUsersRequest(taskUUID))
        );
    }
}
