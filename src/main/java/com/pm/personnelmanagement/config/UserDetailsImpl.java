package com.pm.personnelmanagement.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private final String password;
    private final String username;
    private final boolean isEnabled;
    private final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    public UserDetailsImpl(String password, String username, boolean isEnabled) {
        this.password = password;
        this.username = username;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
