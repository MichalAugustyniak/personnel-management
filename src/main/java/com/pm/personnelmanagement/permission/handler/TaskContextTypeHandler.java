package com.pm.personnelmanagement.permission.handler;

import com.pm.personnelmanagement.permission.constant.ResourceTypes;
import com.pm.personnelmanagement.permission.exception.UnauthorizedException;
import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.permission.repository.PermissionRepository;
import com.pm.personnelmanagement.task.model.Task;
import com.pm.personnelmanagement.task.repository.TaskRepository;
import com.pm.personnelmanagement.task.util.TaskUtils;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.UserUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class TaskContextTypeHandler implements ContextTypeHandler {
    public final static String CONTEXT_TYPE = ResourceTypes.TASK;
    private final TaskUtils taskUtils;

    public TaskContextTypeHandler(PermissionRepository permissionRepository, TaskRepository taskRepository, TaskUtils taskUtils, UserUtils userUtils) {
        this.taskUtils = taskUtils;
    }

    @Override
    public String contextTypeName() {
        return CONTEXT_TYPE;
    }

    /*
    @Override
    public List<PermissionedResource> getIncluded(@NotNull Set<@NotNull Permission> permissions, @NotEmpty String username) {
        User user = userUtils.fetchUserByUsername(username);
        Set<Long> ids = permissions.stream().map(permission -> Long.valueOf(permission.getContextId())).collect(Collectors.toSet());
        List<Task> tasks = taskUtils.fetchByIds(ids);
        Set<Permission> permissionSet = new HashSet<>();
        for (var task : tasks) {
            if (!task.getUsers().contains(user)) {
                continue;
            }
            permissionSet.addAll(
                    permissions.stream().filter(permission ->
                            permission.getResourceId().equals(
                                    String.valueOf(task.getId())
                            )).collect(Collectors.toSet())
            );
        }
        Map<String, Set<Permission>> resourceIdPermissionSetMap = new HashMap<>();
        for (var permission : permissionSet) {
            if (!resourceIdPermissionSetMap.containsKey(permission.getResourceId())) {
                resourceIdPermissionSetMap.put(permission.getResourceId(), new HashSet<>());
            }
            resourceIdPermissionSetMap.get(permission.getResourceId()).add(permission);
        }
        List<PermissionedResource> permissionedResources = new LinkedList<>();
        for (Map.Entry<String, Set<Permission>> entry : resourceIdPermissionSetMap.entrySet()) {
            permissionedResources.add(
                    new PermissionedResource(entry.getKey(), entry.getValue())
            );
        }
        return permissionedResources;
    }

     */

    @Override
    public boolean isUserAssociated(@NotNull User user, @NotNull Permission permission) {
        if (!permission.getContextType().equals(CONTEXT_TYPE)) {
            throw new UnauthorizedException("You have no access to do that");
        }
        Task task = taskUtils.fetchTaskById(Long.parseLong(permission.getContextId()));
        return task.getCreatedBy().equals(user) || task.getUsers().contains(user);
    }
}
