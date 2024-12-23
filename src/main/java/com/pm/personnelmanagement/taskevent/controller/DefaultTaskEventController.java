package com.pm.personnelmanagement.taskevent.controller;

import com.pm.personnelmanagement.taskevent.dto.EditTaskEventRequest;
import com.pm.personnelmanagement.taskevent.dto.TaskEventDTO;
import com.pm.personnelmanagement.taskevent.dto.TaskEventRequest;
import com.pm.personnelmanagement.taskevent.dto.TaskEventsRequest;
import com.pm.personnelmanagement.taskevent.service.TaskEventService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-events")
public class DefaultTaskEventController implements TaskEventController {

    private final TaskEventService taskEventService;

    public DefaultTaskEventController(TaskEventService taskEventService) {
        this.taskEventService = taskEventService;
    }

    @Override
    @PostMapping
    public ResponseEntity<Void> createTaskEvent(@RequestBody TaskEventRequest request) {
        taskEventService.createTaskEvent(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Void> editTaskEvent(@PathVariable long id, @RequestBody TaskEventRequest request) {
        taskEventService.editTaskEvent(new EditTaskEventRequest(id, request));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskEvent(@PathVariable long id) {
        taskEventService.deleteTaskEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TaskEventDTO> getTaskEventById(@PathVariable long id) {
        return ResponseEntity.ok(taskEventService.getTaskEventById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<TaskEventDTO>> getAllTaskEvents(
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ) {
        return ResponseEntity.ok(taskEventService.getAllTaskEvents(new TaskEventsRequest(pageNumber, pageSize)));
    }
}
