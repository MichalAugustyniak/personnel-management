package com.pm.personnelmanagement.permission.handler;

import com.pm.personnelmanagement.permission.constant.ResourceTypes;
import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.schedule.exception.UserScheduleNotFoundException;
import com.pm.personnelmanagement.schedule.model.Schedule;
import com.pm.personnelmanagement.schedule.model.UserSchedule;
import com.pm.personnelmanagement.schedule.utils.ScheduleUtils;
import com.pm.personnelmanagement.user.model.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ScheduleContextTypeHandler implements ContextTypeHandler {
    public static final String CONTEXT_TYPE = ResourceTypes.SCHEDULE;
    private final ScheduleUtils scheduleUtils;

    public ScheduleContextTypeHandler(ScheduleUtils scheduleUtils) {
        this.scheduleUtils = scheduleUtils;
    }

    @Override
    public String contextTypeName() {
        return CONTEXT_TYPE;
    }

    @Override
    public boolean isUserAssociated(@NotNull User user, @NotNull Permission permission) {
        Schedule schedule = scheduleUtils.fetchScheduleById(Long.parseLong(permission.getContextId()));
        try {
            UserSchedule userSchedule = scheduleUtils.fetchUserSchedule(user, schedule);
            return false;
        } catch (UserScheduleNotFoundException e) {
            return false;
        }
    }
}
