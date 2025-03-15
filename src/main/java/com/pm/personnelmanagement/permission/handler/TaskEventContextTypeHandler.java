package com.pm.personnelmanagement.permission.handler;

import com.pm.personnelmanagement.permission.constant.ResourceTypes;
import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.task.model.Task;
import com.pm.personnelmanagement.taskevent.model.TaskEvent;
import com.pm.personnelmanagement.taskevent.util.TaskEventUtils;
import com.pm.personnelmanagement.user.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TaskEventContextTypeHandler implements ContextTypeHandler {
    public static final String CONTEXT_TYPE = ResourceTypes.TASK_EVENT;
    private final TaskEventUtils taskEventUtils;

    public TaskEventContextTypeHandler(TaskEventUtils taskEventUtils) {
        this.taskEventUtils = taskEventUtils;
    }

    @Override
    public String contextTypeName() {
        return CONTEXT_TYPE;
    }

    @Override
    public boolean isUserAssociated(User user, Permission permission) {
        TaskEvent taskEvent = taskEventUtils.fetchTaskEventById(Long.parseLong(permission.getContextId()));
        Set<Task> tasks = taskEvent.getTasks();
        for (var task : tasks) {
            if (task.getUsers().contains(user)) {
                return true;
            }
        }
        return false;
    }
}
