package org.advanze.springframework.security.web.access.expression.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.advanze.springframework.security.web.access.expression.dao.InterceptUrlDao;
import org.advanze.springframework.security.web.access.expression.domain.InterceptUrl;
import org.advanze.springframework.security.web.access.expression.service.InterceptUrlService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Setter
public class InterceptUrlServiceImpl implements InterceptUrlService {

    private InterceptUrlDao interceptUrlDao;

    @Transactional(readOnly = true)
    @Override
    public Map<RequestMatcher, Collection<ConfigAttribute>> retrieveUrls() {
        Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        List<InterceptUrl> list = interceptUrlDao.findAll();
        Collections.sort(list);
        log.trace("sorted list {}", list);
        for (InterceptUrl it : list) {
            if (("/**".equals(it.getPath()) || "**".equals(it.getPath())) && it.getMethod() == null) {
                requestMap.put(AnyRequestMatcher.INSTANCE, SecurityConfig.createListFromCommaDelimitedString(it.getAccess()));
                continue;
            }
            requestMap.put(new AntPathRequestMatcher(it.getPath(), it.getMethod()), SecurityConfig.createListFromCommaDelimitedString(it.getAccess()));
        }
        return requestMap;
    }
}
