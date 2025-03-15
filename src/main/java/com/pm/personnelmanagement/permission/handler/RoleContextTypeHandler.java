package com.pm.personnelmanagement.permission.handler;

import com.pm.personnelmanagement.permission.constant.ResourceTypes;
import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.user.model.Role;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.RoleUtils;
import org.springframework.stereotype.Component;

@Component
public class RoleContextTypeHandler implements ContextTypeHandler {
    public static final String CONTEXT_TYPE = ResourceTypes.ROLE;
    private final RoleUtils roleUtils;

    public RoleContextTypeHandler(RoleUtils roleUtils) {
        this.roleUtils = roleUtils;
    }

    @Override
    public String contextTypeName() {
        return CONTEXT_TYPE;
    }

    @Override
    public boolean isUserAssociated(User user, Permission permission) {
        Role role = roleUtils.fetchRoleById(Long.parseLong(permission.getContextId()));
        return role.getUsers().contains(user);
    }
}
