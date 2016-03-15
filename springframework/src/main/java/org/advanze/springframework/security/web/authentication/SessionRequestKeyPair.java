package org.bitbucket.risu8.nuije.springframework.security.web.authentication;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SessionRequestKeyPair {

    private String sessionKey;
    private String requestKey;
}
