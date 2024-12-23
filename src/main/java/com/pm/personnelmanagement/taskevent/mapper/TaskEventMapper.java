package com.pm.personnelmanagement.taskevent.mapper;

import com.pm.personnelmanagement.taskevent.dto.TaskEventDTO;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;

public class TaskEventMapper {
    public static TaskEventDTO map(TaskEvent taskEvent) {
        return new TaskEventDTO(
                taskEvent.getId(),
                taskEvent.getName(),
                taskEvent.getDescription(),
                taskEvent.getStartDateTime(),
                taskEvent.getEndDateTime()
        );
    }
}
