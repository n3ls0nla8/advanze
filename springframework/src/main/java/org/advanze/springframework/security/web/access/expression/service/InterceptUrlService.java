package org.advanze.springframework.security.web.access.expression.service;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Collection;
import java.util.Map;

public interface InterceptUrlService {

    Map<RequestMatcher,Collection<ConfigAttribute>> retrieveUrls();
}
