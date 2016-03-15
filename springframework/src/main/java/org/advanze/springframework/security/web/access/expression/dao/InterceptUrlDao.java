package org.bitbucket.risu8.nuije.springframework.security.web.access.expression.dao;

import org.bitbucket.risu8.nuije.springframework.security.web.access.expression.domain.InterceptUrl;

import java.util.List;

public interface InterceptUrlDao {

    List<InterceptUrl> findAll();
}
