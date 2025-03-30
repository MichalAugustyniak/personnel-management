package com.pm.personnelmanagement.task.service;

import com.pm.personnelmanagement.common.dto.PagedResponse;
import com.pm.personnelmanagement.common.UnauthorizedException;
import com.pm.personnelmanagement.task.dto.*;
import com.pm.personnelmanagement.task.mapper.TaskMapper;
import com.pm.personnelmanagement.task.model.Task;
import com.pm.personnelmanagement.task.repository.TaskRepository;
import com.pm.personnelmanagement.task.util.TaskUtils;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import com.pm.personnelmanagement.taskevent.util.TaskEventUtils;
import com.pm.personnelmanagement.user.constant.DefaultRoleNames;
import com.pm.personnelmanagement.user.mapping.UserMapper;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.UserUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultTaskService implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskUtils taskUtils;
    private final TaskEventUtils taskEventUtils;
    private final UserUtils userUtils;

    public DefaultTaskService(TaskRepository taskRepository, TaskUtils taskUtils, TaskEventUtils taskEventUtils, UserUtils userUtils) {
        this.taskRepository = taskRepository;
        this.taskUtils = taskUtils;
        this.taskEventUtils = taskEventUtils;
        this.userUtils = userUtils;
    }

    @Override
    public TaskCreationResponse createTask(@NotNull @Valid AuthenticatedRequest<TaskCreationRequest> request) {
        User user = userUtils.fetchUserByUsername(request.principalName());
        boolean isAdminOrManager = user.getRole().getName().equals(DefaultRoleNames.ADMIN) || user.getRole().getName().equals(DefaultRoleNames.MANAGER);
        if (!isAdminOrManager) {
            throw new UnauthorizedException("You're not authorized to do that");
        }

        Task task = new Task();

        Optional.ofNullable(request.request().taskEventUUID()).ifPresent(uuid -> {
            TaskEvent taskEvent = taskEventUtils.fetchTaskEvent(uuid);
            task.setTaskEvent(taskEvent);
        });
        task.setName(request.request().name());
        task.setDescription(request.request().description());
        task.setStartDateTime(request.request().startDateTime());
        task.setEndDateTime(request.request().endDateTime());
        task.setColor(request.request().color());
        task.setCreatedBy(user);
        task.setCompleted(request.request().isCompleted());

        taskRepository.save(task);
        return new TaskCreationResponse(task.getUuid());
    }

    @Override
    public void editTask(@NotNull @Valid AuthenticatedRequest<TaskUpdateRequest> request) {
        System.out.println(request);
        User user = userUtils.fetchUserByUsername(request.principalName());
        Task task = taskUtils.fetchTask(request.request().uuid());
        if (user.getRole().getName().equals(DefaultRoleNames.MANAGER) && !task.getCreatedBy().equals(user)) {
            throw new UnauthorizedException("Only owner do that");
        }
        if (!user.getRole().getName().equals(DefaultRoleNames.ADMIN)) {
            throw new UnauthorizedException("You're not authorized to do that");
        }
        Optional.ofNullable(request.request().task().taskEventUUID()).ifPresent(taskEventUUID -> {
            TaskEvent taskEvent = taskEventUtils.fetchTaskEvent(taskEventUUID);
            task.setTaskEvent(taskEvent);
        });
        Optional.ofNullable(request.request().task().isCompleted()).ifPresent(task::setCompleted);
        Optional.ofNullable(request.request().task().name()).ifPresent(task::setName);
        Optional.ofNullable(request.request().task().description()).ifPresent(task::setDescription);
        Optional.ofNullable(request.request().task().startDateTime()).ifPresent(task::setStartDateTime);
        Optional.ofNullable(request.request().task().endDateTime()).ifPresent(task::setEndDateTime);
        Optional.ofNullable(request.request().task().color()).ifPresent(task::setColor);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(@NotNull AuthenticatedRequest<TaskDeletionRequest> request) {
        User user = userUtils.fetchUserByUsername(request.principalName());
        Task task = taskUtils.fetchTask(request.request().uuid());
        if (user.getRole().getName().equals(DefaultRoleNames.MANAGER) && !task.getCreatedBy().equals(user)) {
            throw new UnauthorizedException("Only owner can do that");
        }
        if (!user.getRole().getName().equals(DefaultRoleNames.ADMIN)) {
            throw new UnauthorizedException("You're not authorized to do that");
        }
        taskRepository.delete(task);
    }

    @Override
    public TaskDTO getTask(@NotNull AuthenticatedRequest<TaskRequest> request) {
        Task task = taskUtils.fetchTask(request.request().uuid());
        return TaskMapper.map(task);
    }

    @Override
    public PagedResponse<TaskDTO> getAllTasks(@NotNull AuthenticatedRequest <TasksRequestFilters> request) {
        int pageNumber = Optional.ofNullable(request.request().pageNumber()).orElse(0);
        int pageSize = Optional.ofNullable(request.request().pageSize()).orElse(10);
        Specification<Task> specification = Specification.where(null);
        System.out.println(request.request());
        if (request.request().nameLike() != null) {
            Specification<Task> hasNameLike = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"),"%" + request.request().nameLike() + "%");
            specification = specification.and(hasNameLike);
        }
        if (request.request().createdBy() != null) {
            Specification<Task> hasCreator = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("createdBy").get("username"), request.request().createdBy());
            specification = specification.and(hasCreator);
        }
        if (request.request().taskEventUUID() != null) {
            Specification<Task> hasTaskEvent = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("taskEvent").get("uuid"), request.request().taskEventUUID());
            specification = specification.and(hasTaskEvent);
        }
        if (request.request().from() != null) {
            Specification<Task> hasFrom = (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDateTime"), request.request().from());
            specification = specification.and(hasFrom);
        }
        if (request.request().to() != null) {
            Specification<Task> hasTo = (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDateTime"), request.request().to());
            specification = specification.and(hasTo);
        }
        if (request.request().user() != null/* && isAdminOrManager*/) {
            Specification<Task> hasUser = (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.join("users").get("username"), request.request().user());
            specification = specification.and(hasUser);
        }

        Page<Task> tasks = taskRepository.findAll(specification, PageRequest.of(pageNumber, pageSize));
        return new PagedResponse<>(
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.getNumber(),
                tasks.getNumberOfElements(),
                tasks.getSize(),
                tasks.getContent().stream().map(TaskMapper::map).toList()
        );
    }

    @Override
    public void assignUserToTask(@NotNull @Valid AuthenticatedRequest<AssignUserToTaskRequest> request) {
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        User user = userUtils.fetchUser(request.request().userUUID());
        Task task = taskUtils.fetchTask(request.request().taskUUID());
        boolean isManager = principalUser.getRole().getName().equals(DefaultRoleNames.MANAGER);
        boolean isAdmin = principalUser.getRole().getName().equals(DefaultRoleNames.ADMIN);
        boolean isCreator = principalUser.equals(task.getCreatedBy());
        if (isManager && !isCreator) {
            throw new UnauthorizedException("You has no access to do that");
        }
        if (!isManager && !isAdmin) {
            throw new UnauthorizedException("You has no access to do that");
        }
        task.getUsers().add(user);
        System.out.printf("Adding user %s to task %s%n", user.getUsername(), task.getName());
        taskRepository.save(task);
    }

    @Override
    public void dismissUserFromTask(@NotNull @Valid AuthenticatedRequest<DismissUserFromTaskRequest> request) {
        Task task = taskUtils.fetchTask(request.request().taskUUID());
        User principalUser = userUtils.fetchUserByUsername(request.principalName());
        User user = userUtils.fetchUser(request.request().userUUID());
        boolean isManager = principalUser.getRole().getName().equals(DefaultRoleNames.MANAGER);
        boolean isAdmin = principalUser.getRole().getName().equals(DefaultRoleNames.ADMIN);
        boolean isCreator = principalUser.getUsername().equals(task.getCreatedBy().getUsername());
        if (isManager && !isCreator) {
            throw new UnauthorizedException("You has no access to do that");
        }
        if (!isManager && !isAdmin) {
            throw new UnauthorizedException("You has no access to do that");
        }
        task.getUsers().remove(user);
        taskRepository.save(task);
    }

    @Override
    public TaskUsers getUsersByTask(TaskUsersRequest request) {
        Task task = taskUtils.fetchTask(request.uuid());
        return new TaskUsers(task.getUsers().stream().map(UserMapper::mapToSimplified).toList());
    }
}
