package com.pm.personnelmanagement.user.controller;

import com.pm.personnelmanagement.config.SessionDTO;
import com.pm.personnelmanagement.config.UnauthenticatedException;
import com.pm.personnelmanagement.config.UserDetailsImpl;
import com.pm.personnelmanagement.user.dto.LoginRequest;
import com.pm.personnelmanagement.user.exception.UserNotFoundException;
import com.pm.personnelmanagement.user.model.User;
import com.pm.personnelmanagement.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping
public class DefaultLoginController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final UserRepository userRepository;

    public DefaultLoginController(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public void login(@NotNull @Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        System.out.printf("username=%s\tpassword=%s%n", loginRequest.username(), loginRequest.password());
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authentication);
        SecurityContext securityContext = new SecurityContextImpl(authenticationResponse);
        securityContextRepository.saveContext(securityContext, request, response);
        System.out.println(authenticationResponse.isAuthenticated());
        User user = userRepository.findByUsername(loginRequest.username()).orElseThrow(
                () -> new UserNotFoundException(String.format("User of username %s not found", loginRequest.username()))
        );
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @GetMapping("/session")
    public SessionDTO session(HttpServletRequest request) {
        DeferredSecurityContext context = securityContextRepository.loadDeferredContext(request);
        SecurityContext securityContext = context.get();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthenticatedException("Unauthenticated");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        return new SessionDTO(userDetails.getUsername(), userDetails.getGrantedAuthorities().stream().map(grantedAuthority -> {
            String authority = grantedAuthority.getAuthority();
            if (authority.startsWith("ROLE_")) {
                return authority.replaceFirst("ROLE_", "");
            }
            return authority;
        }).toList());
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        DeferredSecurityContext deferredSecurityContext = securityContextRepository.loadDeferredContext(request);
        Authentication authentication = deferredSecurityContext.get().getAuthentication();
        authentication.setAuthenticated(false);
    }
}
