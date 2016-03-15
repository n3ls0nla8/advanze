package org.bitbucket.risu8.nuije.springframework.security.provisioning.dao;

import org.bitbucket.risu8.nuije.springframework.security.provisioning.domain.Role;

public interface RoleDao {

    Role findByRole(String role);
}
