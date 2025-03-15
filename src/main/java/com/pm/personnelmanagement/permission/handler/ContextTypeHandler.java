package com.pm.personnelmanagement.permission.handler;

import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.user.model.User;

import java.util.List;
import java.util.Set;

/**
 * Checks if user is associated with provided permission
 */
public interface ContextTypeHandler {
    String contextTypeName();
    boolean isUserAssociated(User user, Permission permission);
}
