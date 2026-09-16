package com.driveden.app.application.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.driveden.app.domain.auth.dto.AuthenticatedUser;

@Service
public class SecurityContextService {
    public AuthenticatedUser getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return (AuthenticatedUser) authentication.getPrincipal();
    }

    public Long getAuthenticatedUserId() {
        return getAuthenticatedUser().id();
    }

}
