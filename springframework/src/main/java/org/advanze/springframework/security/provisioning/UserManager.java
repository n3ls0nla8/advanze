package org.bitbucket.risu8.nuije.springframework.security.provisioning;

import org.bitbucket.risu8.nuije.springframework.security.provisioning.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindException;

public interface UserManager extends UserDetailsService {

    void createUser(User user) throws BindException;

    void updateUser(User user) throws BindException;

    void deleteUser(String username) throws BindException;

    boolean userExists(String username);

    void changePassword(String oldPassword, String newPassword);

    void changePasswordNoAuthentication(String username, String oldPassword, String newPassword);
}
