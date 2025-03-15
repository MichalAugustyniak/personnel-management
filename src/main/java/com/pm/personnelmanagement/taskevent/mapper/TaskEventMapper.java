package com.pm.personnelmanagement.taskevent.mapper;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.taskevent.dto.TaskEventDTO;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import org.springframework.data.domain.Page;

public class TaskEventMapper {
    public static TaskEventDTO map(TaskEvent taskEvent) {
        return new TaskEventDTO(
                taskEvent.getUuid(),
                taskEvent.getName(),
                taskEvent.getDescription(),
                taskEvent.getStartDateTime(),
                taskEvent.getEndDateTime(),
                taskEvent.getCreatedBy().getUsername(),
                taskEvent.getCreatedAt()
        );
    }

    public static PagedResponse<TaskEventDTO> map(Page<TaskEvent> taskEvents) {
        return new PagedResponse<>(
                taskEvents.getTotalElements(),
                taskEvents.getTotalPages(),
                taskEvents.getNumber(),
                taskEvents.getNumberOfElements(),
                taskEvents.getSize(),
                taskEvents.getContent().stream().map(TaskEventMapper::map).toList()
        );
    }
}
