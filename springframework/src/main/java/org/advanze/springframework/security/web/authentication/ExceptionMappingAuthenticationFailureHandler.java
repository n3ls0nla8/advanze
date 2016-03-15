package org.bitbucket.risu8.nuije.springframework.security.web.authentication;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Setter
public class ExceptionMappingAuthenticationFailureHandler extends org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        saveException(request, exception);
        super.onAuthenticationFailure(request, response, exception);
    }
}
