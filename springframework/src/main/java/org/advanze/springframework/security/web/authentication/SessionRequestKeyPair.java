package org.advanze.springframework.security.web.authentication;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SessionRequestKeyPair {

    private String sessionKey;
    private String requestKey;
}
