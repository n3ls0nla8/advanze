package org.advanze.springframework.security.provisioning.dao;


import org.advanze.springframework.security.provisioning.domain.User;

public interface UserDao {

    User findByUsername(String username);

    Integer countByUsername(String username);

    void saveOrUpdate(User user);

    void delete(User user);
}
