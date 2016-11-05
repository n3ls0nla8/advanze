package org.advanze.springframework.security.web;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.advanze.springframework.security.web.authentication.SessionRequestKeyPair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.DefaultRedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

@Getter
@Setter
@Slf4j
public class SavedRequestParamRedirectStrategy extends DefaultRedirectStrategy {

    private Set<SessionRequestKeyPair> requestRedirectMapping;

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        for (SessionRequestKeyPair pair : requestRedirectMapping) {
            String sessionKey = pair.getSessionKey();
            String requestParam = pair.getRequestKey();

            String p = StringUtils.trimToEmpty(request.getParameter(requestParam));
            HttpSession session = request.getSession(false);
            if (session != null && !p.isEmpty()) {
                session.setAttribute(sessionKey, p);
            }
        }
        super.sendRedirect(request, response, url);
    }
}
