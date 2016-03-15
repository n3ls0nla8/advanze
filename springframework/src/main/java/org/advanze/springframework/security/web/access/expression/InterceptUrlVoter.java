package org.bitbucket.risu8.nuije.springframework.security.web.access.expression;

import lombok.Setter;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Collection;

public class InterceptUrlVoter extends WebExpressionVoter {

    @Setter
    private SecurityExpressionHandler<FilterInvocation> expressionHandler = new DefaultWebSecurityExpressionHandler();

    public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
        assert authentication != null;
        assert fi != null;
        assert attributes != null;

        WebExpressionSecurityConfig weca = findConfigAttribute(attributes);

        if (weca == null) {
            return ACCESS_ABSTAIN;
        }

        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, fi);

        return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED : ACCESS_DENIED;
    }

    private WebExpressionSecurityConfig findConfigAttribute(Collection<ConfigAttribute> attributes) {
        for (ConfigAttribute attribute : attributes) {
            if (attribute instanceof WebExpressionSecurityConfig) {
                return (WebExpressionSecurityConfig) attribute;
            }
        }
        return null;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return super.supports(attribute) || attribute instanceof WebExpressionSecurityConfig;
    }
}
