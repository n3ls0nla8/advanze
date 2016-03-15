package org.bitbucket.risu8.nuije.springframework.security.access.expression.method;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

@Slf4j
public class DefaultMethodSecurityExpressionHandler extends org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler {

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        MethodSecurityExpressionOperations root = super.createSecurityExpressionRoot(authentication, invocation);
        PermissionEvaluator evaluator = getPermissionEvaluator();
        if (!(evaluator instanceof org.bitbucket.risu8.nuije.springframework.security.access.PermissionEvaluator)) {
            return root;
        }
        return new MethodSecurityExpressionRootDecorator(authentication, (org.bitbucket.risu8.nuije.springframework.security.access.PermissionEvaluator) evaluator, root);
    }
}
