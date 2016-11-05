package org.advanze.springframework.security.access.expression.method;

import lombok.extern.slf4j.Slf4j;
import org.advanze.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

@Slf4j
public class MethodSecurityExpressionRootDecorator extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private final org.springframework.security.access.expression.method.MethodSecurityExpressionOperations operations;
    private PermissionEvaluator permissionEvaluator;

    public MethodSecurityExpressionRootDecorator(Authentication authentication, PermissionEvaluator permissionEvaluator, org.springframework.security.access.expression.method.MethodSecurityExpressionOperations methodSecurityExpressionRoot) {
        super(authentication);
        this.operations = methodSecurityExpressionRoot;
        this.permissionEvaluator = permissionEvaluator;
    }

    @Override
    public boolean hasPermission(Object permission) {
        return permissionEvaluator.hasPermission(getAuthentication(), permission);
    }

    public void setFilterObject(Object filterObject) {
        operations.setFilterObject(filterObject);
    }

    public Object getFilterObject() {
        return operations.getFilterObject();
    }

    public void setReturnObject(Object returnObject) {
        operations.setReturnObject(returnObject);
    }

    public Object getReturnObject() {
        return operations.getReturnObject();
    }

    public Object getThis() {
        return operations.getThis();
    }
}