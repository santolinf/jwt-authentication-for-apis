package com.manning.liveproject.simplysend.auth.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {

    public static String getAuthenticatedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static boolean isAuthenticatedUsername(String emailId) {
        return getAuthenticatedUsername().equals(emailId);
    }

    private SecurityHelper() {}
}
