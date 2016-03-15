package org.bitbucket.risu8.nuije.springframework.security.provisioning.dao;

import org.bitbucket.risu8.nuije.springframework.security.provisioning.domain.User;

public interface UserDao {

    User findByUsername(String username);

    Integer countByUsername(String username);

    void saveOrUpdate(User user);

    void delete(User user);
}
