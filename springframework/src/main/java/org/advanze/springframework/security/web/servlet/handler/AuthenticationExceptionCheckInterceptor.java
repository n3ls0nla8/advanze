package org.advanze.springframework.security.web.servlet.handler;

import lombok.Setter;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Setter
public class AuthenticationExceptionCheckInterceptor extends HandlerInterceptorAdapter {

    private String redirectUrl;
    private Class<Exception> exceptionClass;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object exception = request.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (exception != null && exceptionClass.isAssignableFrom(exception.getClass())) {
            return super.preHandle(request, response, handler);
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            exception = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null && exceptionClass.isAssignableFrom(exception.getClass())) {
                return super.preHandle(request, response, handler);
            }
        }

        response.sendRedirect(redirectUrl);
        return false;
    }
}
