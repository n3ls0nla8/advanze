package org.advanze.springframework.security.access;

import org.springframework.security.core.Authentication;

public interface PermissionEvaluator extends org.springframework.security.access.PermissionEvaluator {

    boolean hasPermission(Authentication authentication, Object permission);
}
