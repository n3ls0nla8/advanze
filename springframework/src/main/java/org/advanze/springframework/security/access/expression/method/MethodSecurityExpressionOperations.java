package org.advanze.springframework.security.access.expression.method;

public interface MethodSecurityExpressionOperations extends org.springframework.security.access.expression.method.MethodSecurityExpressionOperations {

    boolean hasPermission(Object permission);
}
