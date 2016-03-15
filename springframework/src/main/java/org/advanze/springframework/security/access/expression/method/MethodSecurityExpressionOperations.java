package org.bitbucket.risu8.nuije.springframework.security.access.expression.method;

public interface MethodSecurityExpressionOperations extends org.springframework.security.access.expression.method.MethodSecurityExpressionOperations {

    boolean hasPermission(Object permission);
}
