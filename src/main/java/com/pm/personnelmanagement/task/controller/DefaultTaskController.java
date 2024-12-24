package com.pm.personnelmanagement.task.controller;

import com.pm.personnelmanagement.task.dto.*;
import com.pm.personnelmanagement.task.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class DefaultTaskController implements TaskController {

    private final TaskService taskService;

    public DefaultTaskController(@Qualifier("defaultTaskService") TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(@RequestBody CreateTaskRequest request, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION); // todo: decode and check if user has access
        String username = "username"; // todo: remove the placeholder

        UUID uuid = taskService.createTask(new CreateTaskDTO(
                username,
                request
        ));
        return new ResponseEntity<>(new CreateTaskResponse(uuid.toString()), HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Void> editTask(@RequestBody EditTaskRequest task, @PathVariable long id, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String username = "username";
        taskService.editTask(new EditTaskDTO(id, task));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable long id, HttpServletRequest httpServletRequest) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<TaskDTO>> getAllTasks(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(taskService.getAllTasks(pageNumber, pageSize));
    }

    @Override
    @PostMapping("/{taskId}/users")
    public ResponseEntity<Void> assignUserToTask(@RequestBody UserTaskRequest request, @RequestParam long taskId) {
        taskService.assignUserToTask(request, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{taskId}/users")
    public ResponseEntity<Void> dismissUserFromTask(@RequestBody UserTaskRequest request, @RequestParam long taskId) {
        taskService.dismissUserFromTask(request, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
