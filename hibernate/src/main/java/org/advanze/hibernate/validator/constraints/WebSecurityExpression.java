package org.bitbucket.risu8.nuije.hibernate.validator.constraints;

import org.bitbucket.risu8.nuije.hibernate.validator.constraints.constraintvalidator.WebSecurityExpressionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy={WebSecurityExpressionValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSecurityExpression {

    String message() default "{org.bitbucket.risu8.hibernate.validator.constraints.WebSecurityExpression.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        WebSecurityExpression[] value();
    }
}
