package com.pm.personnelmanagement.user.api;

import com.pm.personnelmanagement.user.dto.Token;

public interface KeycloakApi {
    Token getToken(String username, String password);
}
