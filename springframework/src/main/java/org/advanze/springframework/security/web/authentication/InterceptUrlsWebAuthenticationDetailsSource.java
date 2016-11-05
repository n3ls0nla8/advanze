package org.advanze.springframework.security.web.authentication;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.advanze.springframework.security.web.access.expression.service.InterceptUrlService;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

@Setter
@Slf4j
public class InterceptUrlsWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, InterceptUrlsWebAuthenticationDetails> {

    private InterceptUrlService interceptUrlService;

    @Override
    public InterceptUrlsWebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new InterceptUrlsWebAuthenticationDetails(request, interceptUrlService.retrieveUrls());
    }
}
