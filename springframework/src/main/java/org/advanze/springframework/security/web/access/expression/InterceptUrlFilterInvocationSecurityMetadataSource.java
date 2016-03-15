package org.bitbucket.risu8.nuije.springframework.security.web.access.expression;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bitbucket.risu8.nuije.springframework.security.web.access.expression.service.InterceptUrlService;
import org.bitbucket.risu8.nuije.springframework.security.web.authentication.InterceptUrlsWebAuthenticationDetails;
import org.springframework.expression.ParseException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Setter
public class InterceptUrlFilterInvocationSecurityMetadataSource extends DefaultFilterInvocationSecurityMetadataSource {

    private SecurityExpressionHandler<FilterInvocation> expressionHandler;
    private InterceptUrlService interceptUrlService;

    public InterceptUrlFilterInvocationSecurityMetadataSource() {
        super(null);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : processMap(retrieve()).entrySet()) {
            allAttributes.addAll(entry.getValue());
        }
        return allAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : processMap(retrieve()).entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> processMap(Map<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestToExpressionAttributesMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>(requestMap);

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            RequestMatcher request = entry.getKey();
            Assert.isTrue(entry.getValue().size() == 1, "Expected a single expression attribute for " + request);
            ArrayList<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>(1);
            String expression = entry.getValue().toArray(new ConfigAttribute[1])[0].getAttribute();
            log.debug("Adding web access control expression '{}', for {}", expression, request);
            try {
                attributes.add(new WebExpressionSecurityConfig(expression, expressionHandler.getExpressionParser().parseExpression(expression)));
            }
            catch (ParseException e) {
                throw new IllegalArgumentException("Failed to parse expression '" + expression + "'");
            }
            requestToExpressionAttributesMap.put(request, attributes);
        }

        return requestToExpressionAttributesMap;
    }

    private Map<RequestMatcher, Collection<ConfigAttribute>> retrieve() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object details = auth.getDetails();
            if (details instanceof InterceptUrlsWebAuthenticationDetails) {
                InterceptUrlsWebAuthenticationDetails o = (InterceptUrlsWebAuthenticationDetails) details;
                return o.getInterceptUrls();
            }
        }
        return interceptUrlService.retrieveUrls();
    }
}
