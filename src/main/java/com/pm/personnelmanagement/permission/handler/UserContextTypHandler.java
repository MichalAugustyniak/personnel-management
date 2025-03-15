package com.pm.personnelmanagement.permission.handler;

import com.pm.personnelmanagement.permission.constant.ResourceTypes;
import com.pm.personnelmanagement.permission.model.Permission;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.UserUtils;
import org.springframework.stereotype.Component;

@Component
public class UserContextTypHandler implements ContextTypeHandler {
    public final static String CONTEXT_TYPE = ResourceTypes.USER;
    private final UserUtils userUtils;

    public UserContextTypHandler(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @Override
    public String contextTypeName() {
        return CONTEXT_TYPE;
    }

    @Override
    public boolean isUserAssociated(User user, Permission permission) {
        return userUtils.existById(user.getId());
    }
}
