package com.pm.personnelmanagement.taskevent.service;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.common.util.SpecificationUtils;
import com.pm.personnelmanagement.permission.exception.UnauthorizedException;
import com.pm.personnelmanagement.task.dto.AuthenticatedRequest;
import com.pm.personnelmanagement.task.model.Task;
import com.pm.personnelmanagement.taskevent.dto.*;
import com.pm.personnelmanagement.taskevent.mapper.TaskEventMapper;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import com.pm.personnelmanagement.taskevent.repository.TaskEventRepository;
import com.pm.personnelmanagement.taskevent.util.TaskEventUtils;
import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.UserUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Validated
public class DefaultTaskEventService implements TaskEventService {
    private final TaskEventRepository taskEventRepository;
    private final UserUtils userUtils;
    private final TaskEventUtils taskEventUtils;

    public DefaultTaskEventService(TaskEventRepository taskEventRepository, UserUtils userUtils, TaskEventUtils taskEventUtils) {
        this.taskEventRepository = taskEventRepository;
        this.userUtils = userUtils;
        this.taskEventUtils = taskEventUtils;
    }

    @Override
    public TaskEventCreationResponse createTaskEvent(AuthenticatedRequest<TaskEventCreationRequest> request) {
        User principal = userUtils.fetchUserByUsername(request.principalName());
        boolean isAdminOrManager = principal.getRole().getName().equals(DefaultRoleNames.ADMIN) ||
                principal.getRole().getName().equals(DefaultRoleNames.MANAGER);
        if (!isAdminOrManager) {
            throw new UnauthorizedException("You have unauthorized role");
        }
        TaskEvent taskEvent = new TaskEvent();
        var requestBody = request.request();
        taskEvent.setName(requestBody.name());
        taskEvent.setDescription(requestBody.description());
        taskEvent.setStartDateTime(requestBody.startDateTime());
        taskEvent.setEndDateTime(requestBody.endDateTime());
        taskEvent.setCreatedBy(principal);
        taskEvent.setCreatedAt(LocalDateTime.now());
        taskEventRepository.save(taskEvent);
        return new TaskEventCreationResponse(taskEvent.getUuid());
    }

    @Override
    public void editTaskEvent(AuthenticatedRequest<TaskEventUpdateRequest> request) {
        var requestBody = request.request();
        User principal = userUtils.fetchUserByUsername(request.principalName());
        boolean isAdmin = principal.getRole().getName().equals(DefaultRoleNames.ADMIN);
        boolean isManager = principal.getRole().getName().equals(DefaultRoleNames.MANAGER);
        if (!(isAdmin || isManager)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        TaskEvent taskEvent = taskEventUtils.fetchTaskEvent(requestBody.uuid());
        boolean isAuthor = taskEvent.getCreatedBy().equals(principal);
        if (!isAuthor && !isAdmin) {
            throw new UnauthorizedException("You have no access to do that");
        }
        Optional.ofNullable(requestBody.taskEvent().name()).ifPresent(taskEvent::setName);
        Optional.ofNullable(requestBody.taskEvent().description()).ifPresent(taskEvent::setDescription);
        Optional.ofNullable(requestBody.taskEvent().startDateTime()).ifPresent(taskEvent::setStartDateTime);
        Optional.ofNullable(requestBody.taskEvent().endDateTime()).ifPresent(taskEvent::setEndDateTime);
        taskEventRepository.save(taskEvent);
    }

    @Override
    public void deleteTaskEvent(AuthenticatedRequest<TaskEventDeletionRequest> request) {
        var requestBody = request.request();
        User principal = userUtils.fetchUserByUsername(request.principalName());
        boolean isAdmin = principal.getRole().getName().equals(DefaultRoleNames.ADMIN);
        boolean isManager = principal.getRole().getName().equals(DefaultRoleNames.MANAGER);
        if (!(isAdmin || isManager)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        TaskEvent taskEvent = taskEventUtils.fetchTaskEvent(requestBody.uuid());
        boolean isAuthor = taskEvent.getCreatedBy().equals(principal);
        if (!isAuthor && !isAdmin) {
            throw new UnauthorizedException("You have no access to do that");
        }
        taskEventRepository.delete(taskEvent);
    }

    @Override
    public TaskEventDTO getTaskEvent(AuthenticatedRequest<TaskEventRequest> request) {
        var requestBody = request.request();
        TaskEvent taskEvent = taskEventUtils.fetchTaskEvent(requestBody.uuid());
        User principal = userUtils.fetchUserByUsername(request.principalName());
        boolean isMember = taskEventRepository.hasUser(taskEvent, principal);
        boolean isAdmin = principal.getRole().getName().equals(DefaultRoleNames.ADMIN);
        if (!isAdmin && !isMember) {
            throw new UnauthorizedException("You have no access to do that");
        }
        return TaskEventMapper.map(taskEvent);
    }

    @Override
    public PagedResponse<TaskEventDTO> getAllTaskEvents(AuthenticatedRequest<TaskEventsRequest> request) {
        var requestBody = request.request();
        User principal = userUtils.fetchUserByUsername(request.principalName());
        boolean isAdmin = principal.getRole().getName().equals(DefaultRoleNames.ADMIN);
        boolean isManager = principal.getRole().getName().equals(DefaultRoleNames.MANAGER);
        boolean isEmployee = principal.getRole().getName().equals(DefaultRoleNames.EMPLOYEE);
        Specification<TaskEvent> specification = Specification.where(null);

        if (requestBody.createdBy() != null) {
            Specification<TaskEvent> spec = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("createdBy").get("username"), requestBody.createdBy());
            specification = specification.and(spec);
        }
        if (requestBody.from() != null) {
            Specification<TaskEvent> hasFrom = (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), requestBody.from());
            specification = specification.and(hasFrom);
        }
        if (requestBody.to() != null) {
            Specification<TaskEvent> hasTo = (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDateTime"), requestBody.to());
            specification = specification.and(hasTo);
        }
        if (requestBody.user() != null) {
            Specification<TaskEvent> hasUser = (root, query, criteriaBuilder) -> {
                Join<TaskEvent, Task> tasks = root.join("tasks");
                Join<Task, User> users = tasks.join("users");
                Predicate predicate = criteriaBuilder.equal(users.get("username"), requestBody.user());
                return criteriaBuilder.and(predicate);
            };
            specification = specification.and(hasUser);
        }
        if (requestBody.like() != null) {
            Specification<TaskEvent> hasLike = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + requestBody.like() + "%");
            specification = specification.and(hasLike);
        }
        Page<TaskEvent> taskEvents = taskEventUtils.fetchTaskEvents(specification, requestBody.pageNumber(), requestBody.pageSize());
        return TaskEventMapper.map(taskEvents);
    }
}
