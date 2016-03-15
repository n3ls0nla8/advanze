package org.bitbucket.risu8.nuije.springframework.security.provisioning.bean;

public interface CredentialsHolder {

    String getUsername();

    String getPassword();

    void setPassword(String password);
}
