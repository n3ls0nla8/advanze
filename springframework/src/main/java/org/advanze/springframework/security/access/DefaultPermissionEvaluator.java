package org.bitbucket.risu8.nuije.springframework.security.access;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class DefaultPermissionEvaluator extends DenyAllPermissionEvaluator implements PermissionEvaluator, ApplicationContextAware {

    @Setter
    private Set<PermissionResolver> permissionResolvers = new HashSet<PermissionResolver>(Arrays.asList(new RolePermissionResolver()));
    private ApplicationContext applicationContext;

    @Override
    public boolean hasPermission(Authentication authentication, Object permission) {
        if (authentication == null || !(permission instanceof String)) {
            return false;
        }
        if (!authentication.isAuthenticated()) {
            return false;
        }
        try {
            String perm = ((String) permission).trim();
            for (PermissionResolver resolver : retrieve()) {
                if (resolver.resolvePermission(authentication, perm)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private Set<PermissionResolver> retrieve() {
        Set<PermissionResolver> resolvers = new HashSet<PermissionResolver>(permissionResolvers);
        resolvers.addAll(applicationContext.getBeansOfType(PermissionResolver.class).values());
        return resolvers;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
