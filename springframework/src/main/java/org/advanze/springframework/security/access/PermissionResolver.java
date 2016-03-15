package org.bitbucket.risu8.nuije.springframework.security.access;

import org.springframework.security.core.Authentication;

public interface PermissionResolver {

    boolean resolvePermission(Authentication authentication, String permission);
}
