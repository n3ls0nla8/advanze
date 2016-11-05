package org.advanze.springframework.security.web.authentication;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.advanze.springframework.security.provisioning.dao.UserDao;
import org.advanze.springframework.security.provisioning.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

@Slf4j
@Setter
public class SavedRequestAwareAuthenticationSuccessHandler extends org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler  {

    private UserDao userDao;
    private Set<SessionRequestKeyPair> requestRedirectMapping;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User u = userDao.findByUsername(authentication.getName());
        if (u != null) {
            Date now = new Date();
            Integer newAttempt = 0;
            if (u.getLoginAttempt() >= 0) {
                newAttempt += u.getLoginAttempt();
            }
            u.setLastLoginSuccessDate(now);
            u.setLoginAttempt(newAttempt);
            log.debug("Found user {}, update login success date {} and attempt {}", new Object[]{authentication.getName(), now, newAttempt});
            userDao.saveOrUpdate(u);
        }

        HttpSession session = request.getSession(false);
        for (SessionRequestKeyPair p : requestRedirectMapping) {
            String sessionKey = p.getSessionKey();
            if (session.getAttribute(sessionKey) != null) {
                log.debug("Remove saved redirect request parameter {}:{}", sessionKey, session.getAttribute(sessionKey));
                session.removeAttribute(sessionKey);
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
