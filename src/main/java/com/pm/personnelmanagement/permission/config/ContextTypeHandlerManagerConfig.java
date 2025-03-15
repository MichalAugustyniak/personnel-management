package com.pm.personnelmanagement.permission.config;

import com.pm.personnelmanagement.permission.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class ContextTypeHandlerManagerConfig {
    @Bean
    public ContextTypeHandlerManager contextTypeHandlerManager(
            ScheduleContextTypeHandler scheduleContextTypeHandler,
            RoleContextTypeHandler roleContextTypeHandler,
            UserContextTypHandler userContextTypHandler,
            TaskContextTypeHandler taskContextTypeHandler,
            TaskEventContextTypeHandler taskEventContextTypeHandler
    ) {
        List<ContextTypeHandler> contextTypeHandlers = new LinkedList<>();
        contextTypeHandlers.add(scheduleContextTypeHandler);
        contextTypeHandlers.add(roleContextTypeHandler);
        contextTypeHandlers.add(userContextTypHandler);
        contextTypeHandlers.add(taskContextTypeHandler);
        contextTypeHandlers.add(taskEventContextTypeHandler);
        return new ContextTypeHandlerManager(contextTypeHandlers);
    }
}
