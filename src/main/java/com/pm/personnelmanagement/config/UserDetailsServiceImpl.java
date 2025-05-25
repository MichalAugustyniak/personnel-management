package com.pm.personnelmanagement.config;

import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.util.UserUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserUtils userUtils;

    public UserDetailsServiceImpl(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userUtils.fetchUserByUsername(username);
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getPassword(), user.getUsername(), user.getActive());
        userDetails.getGrantedAuthorities().add(new GrantedAuthorityImpl("ROLE_" + user.getRole().getName()));
        return userDetails;
    }
}
