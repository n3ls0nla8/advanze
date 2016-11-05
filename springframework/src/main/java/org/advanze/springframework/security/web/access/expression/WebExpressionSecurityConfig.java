package org.advanze.springframework.security.web.access.expression;

import org.springframework.expression.Expression;
import org.springframework.security.access.SecurityConfig;

public class WebExpressionSecurityConfig extends SecurityConfig {

    private final Expression authorizeExpression;

    public WebExpressionSecurityConfig(String attribute, Expression authorizeExpression) {
        super(attribute);
        this.authorizeExpression = authorizeExpression;
    }

    Expression getAuthorizeExpression() {
        return authorizeExpression;
    }

    @Override
    public String toString() {
        return "AuthorizeExpression: " + authorizeExpression.getExpressionString() + ", Attribute: " + super.toString();
    }
}
