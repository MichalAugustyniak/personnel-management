package com.pm.personnelmanagement.permission.handler;

import com.pm.personnelmanagement.permission.exception.ContextTypeHandlerManagerException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContextTypeHandlerManager {
    private final Map<String, ContextTypeHandler> contextTypeHandlers;

    public ContextTypeHandlerManager(@NotNull Map<String, ContextTypeHandler> contextTypeHandlers) {
        this.contextTypeHandlers = contextTypeHandlers;
    }

    public ContextTypeHandlerManager(@NotNull List<@NotNull ContextTypeHandler> contextTypeHandlers) {
        this.contextTypeHandlers = new HashMap<>();
        for (var contextTypeHandler : contextTypeHandlers) {
            if (this.contextTypeHandlers.containsKey(contextTypeHandler.contextTypeName())) {
                throw new ContextTypeHandlerManagerException(String.format("Duplicate key %s", contextTypeHandler.contextTypeName()));
            }
            this.contextTypeHandlers.put(contextTypeHandler.contextTypeName(), contextTypeHandler);
        }
    }

    public ContextTypeHandler getContextTypeHandler(@NotEmpty String contextType) {
        ContextTypeHandler contextTypeHandler = this.contextTypeHandlers.get(contextType);
        return Optional.ofNullable(contextTypeHandler).orElseThrow(
                () -> new ContextTypeHandlerManagerException(String.format("No ContextTypeHandler implementation found for provided name %s", contextType))
        );
    }

    public void add(@NotNull ContextTypeHandler contextTypeHandler) {
        if (this.contextTypeHandlers.containsKey(contextTypeHandler.contextTypeName())) {
            throw new ContextTypeHandlerManagerException(String.format("Duplicate key %s", contextTypeHandler.contextTypeName()));
        }
        this.contextTypeHandlers.put(contextTypeHandler.contextTypeName(), contextTypeHandler);
    }
}
