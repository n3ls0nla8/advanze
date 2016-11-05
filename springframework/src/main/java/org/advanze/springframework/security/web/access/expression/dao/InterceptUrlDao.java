package org.advanze.springframework.security.web.access.expression.dao;

import org.advanze.springframework.security.web.access.expression.domain.InterceptUrl;

import java.util.List;

public interface InterceptUrlDao {

    List<InterceptUrl> findAll();
}
