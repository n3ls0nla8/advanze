package org.advanze.springframework.security.provisioning.dao;


import org.advanze.springframework.security.provisioning.domain.Role;

public interface RoleDao {

    Role findByRole(String role);
}
