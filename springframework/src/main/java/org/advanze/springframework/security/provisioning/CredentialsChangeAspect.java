package org.bitbucket.risu8.nuije.springframework.security.provisioning;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.bitbucket.risu8.nuije.springframework.security.provisioning.bean.CredentialsHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.util.Assert;

@Slf4j
@Aspect
public class CredentialsChangeAspect {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before("@annotation(credentialsChange) && args(user)")
    public void encodePassword(CredentialsChange credentialsChange, CredentialsHolder user) {
        Assert.notNull(user, "Password holder must not be null");
        String pass = StringUtils.trimToEmpty(user.getPassword());
        if (!pass.isEmpty()) {
            user.setPassword(passwordEncoder.encodePassword(pass, user.getUsername()));
        }
    }
}
