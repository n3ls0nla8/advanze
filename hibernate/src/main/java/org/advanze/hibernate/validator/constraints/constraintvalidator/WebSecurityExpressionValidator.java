package org.advanze.hibernate.validator.constraints.constraintvalidator;

import org.bitbucket.risu8.nuije.hibernate.validator.constraints.WebSecurityExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.Serializable;

public class WebSecurityExpressionValidator implements ConstraintValidator<WebSecurityExpression, Serializable> {

    @Autowired
    private SecurityExpressionHandler<FilterInvocation> expressionHandler;

    @Override
    public void initialize(WebSecurityExpression constraintAnnotation) {}

    @Override
    public boolean isValid(Serializable value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            ExpressionUtils.evaluateAsBoolean(expressionHandler.getExpressionParser().parseExpression(value.toString()), expressionHandler.createEvaluationContext(SecurityContextHolder.getContext().getAuthentication(), new FilterInvocation(request.getServletPath(), request.getMethod())));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
