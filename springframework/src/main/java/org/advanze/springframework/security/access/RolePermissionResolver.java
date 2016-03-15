package org.bitbucket.risu8.nuije.springframework.security.access;

import lombok.extern.slf4j.Slf4j;
import org.bitbucket.risu8.nuije.springframework.security.provisioning.domain.Role;
import org.bitbucket.risu8.nuije.springframework.security.provisioning.domain.Grant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class RolePermissionResolver extends PermissionResolverSupport {

    @Override
    @Transactional(readOnly = true)
    public boolean resolvePermission(Authentication authentication, String permission) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (!(authority instanceof Role)) {
                continue;
            }
            for (Grant rp : ((Role) authority).getGrants()) {
                if (permission.equals(rp.getPermission().getName())) {
                    return true;
                }
            }
        }
        return super.resolvePermission(authentication, permission);
    }
}
