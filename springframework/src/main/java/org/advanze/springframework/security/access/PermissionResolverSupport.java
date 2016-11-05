package org.advanze.springframework.security.access;

import org.springframework.security.core.Authentication;

public abstract class PermissionResolverSupport implements PermissionResolver {

    @Override
    public boolean resolvePermission(Authentication authentication, String permission) {
        return false;
    }
}
